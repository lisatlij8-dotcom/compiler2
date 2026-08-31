import AST.AstNode;
import AST_H_C.CSS_Style;
import AST_H_C.Node;
import CodeGeneration.CodeGenerationException;
import CodeGeneration.Generator;
import Semantic.ProjectSemanticContext;
import Semantic.SemanticError;
import Semantic.SemanticErrorReporter;
import SymbolTable.Scope;
import grammers.cssLexer;
import grammers.cssParser;
import grammers.flaskLexer;
import grammers.flaskParser;
import grammers.htmlLexer;
import grammers.htmlParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import visitor.CssVisitor;
import visitor.HtmlVisitor;
import visitor.PythonSemanticVisitor;
import visitor.PythonVisitor;
import visitor.SymbolTableVisitor;
import visitor.WebSemanticVisitor;
import visitor.WebSymbolTableVisitor;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    private static final String PYTHON_SOURCE = "demo_flask/app.py";
    private static final String TEMPLATE_DIRECTORY = "demo_flask/templates";
    private static final String STANDALONE_CSS_DIRECTORY = "demo_flask/static/css";
    private static final String GENERATED_OUTPUT_DIRECTORY = "generated_output";

    public static void main(String[] args) {
        Scope.clearReport();
        WebSymbolTableVisitor.clearReport();

        ProjectSemanticContext projectContext = new ProjectSemanticContext();
        SemanticErrorReporter pythonReporter = new SemanticErrorReporter();
        SemanticErrorReporter webReporter = new SemanticErrorReporter();
        SemanticErrorReporter projectReporter = new SemanticErrorReporter();
        SemanticErrorReporter finalReporter = new SemanticErrorReporter();

        int pythonSyntaxErrors = 0;
        int webSyntaxErrors = 0;

        printHeader();

        PythonResult pythonResult = runPythonPipeline(PYTHON_SOURCE, pythonReporter);
        pythonSyntaxErrors += pythonResult.syntaxErrors;
        if (pythonResult.semanticVisitor != null) {
            projectContext.addPythonResults(pythonResult.semanticVisitor, PYTHON_SOURCE);
        }

        System.out.println();
        System.out.println("[2] WEB TEMPLATES");
        System.out.println("--------------------------------------------------");

        List<Path> templatePaths = discoverTemplates();
        List<String> analyzedTemplates = new ArrayList<>();
        Map<String, Node> templateAsts = new LinkedHashMap<>();
        for (Path templatePath : templatePaths) {
            TemplateResult templateResult = runTemplatePipeline(templatePath, webReporter);
            webSyntaxErrors += templateResult.syntaxErrors;
            if (templateResult.semanticVisitor != null) {
                String templateName = templatePath.getFileName().toString();
                String sourceFile = templatePath.toString();
                projectContext.addTemplate(templateName, templateResult.semanticVisitor, sourceFile);
                analyzedTemplates.add(templateName);
                templateAsts.put(templateName, templateResult.ast);
            }
        }

        System.out.println();
        System.out.println("[2b] STANDALONE CSS");
        System.out.println("--------------------------------------------------");

        List<Path> cssPaths = discoverStandaloneCssFiles();
        List<String> analyzedCssFiles = new ArrayList<>();
        int cssSyntaxErrors = 0;
        for (Path cssPath : cssPaths) {
            CssResult cssResult = runCssPipeline(cssPath, webReporter);
            cssSyntaxErrors += cssResult.syntaxErrors;
            if (cssResult.ast != null) {
                projectContext.addStandaloneCss(cssResult.sourceFile, cssResult.ast.getRuleSets());
                analyzedCssFiles.add(cssPath.getFileName().toString());
            }
        }

        System.out.println();
        System.out.println("WEB SYMBOL TABLE:");
        WebSymbolTableVisitor.printReport();

        projectContext.validate(projectReporter);

        copyDiagnostics(pythonReporter, finalReporter);
        copyDiagnostics(webReporter, finalReporter);
        copyDiagnostics(projectReporter, finalReporter);

        printSemanticSection(pythonReporter, webReporter, projectReporter, finalReporter);
        runCodeGeneration(pythonResult, templateAsts, pythonSyntaxErrors, webSyntaxErrors,
                cssSyntaxErrors, finalReporter);
        printSummary(pythonSyntaxErrors, webSyntaxErrors, cssSyntaxErrors, finalReporter,
                analyzedTemplates, analyzedCssFiles);
    }

    /**
     * The project's Code Generation phase: takes the Python AST's data,
     * resolves it against each render_template(...) call's context, and
     * renders the matching template's Web/Jinja AST into final HTML written
     * under generated_output/. This is our own compiler output stage - not
     * to be confused with ANTLR generating grammers/*.g4 into
     * gen/grammers/*.java, which happens before the compiler runs at all.
     * Only runs when parsing succeeded and no semantic errors exist.
     */
    private static void runCodeGeneration(PythonResult pythonResult,
                                          Map<String, Node> templateAsts,
                                          int pythonSyntaxErrors,
                                          int webSyntaxErrors,
                                          int cssSyntaxErrors,
                                          SemanticErrorReporter finalReporter) {
        System.out.println();
        System.out.println("[4] CODE GENERATION");
        System.out.println("--------------------------------------------------");

        boolean canGenerate = pythonSyntaxErrors == 0
                && webSyntaxErrors == 0
                && cssSyntaxErrors == 0
                && !finalReporter.hasErrors();

        if (!canGenerate) {
            System.out.println("Skipped: compilation has syntax or semantic errors.");
            return;
        }
        if (pythonResult.root == null || pythonResult.semanticVisitor == null) {
            System.out.println("Skipped: no Python source available for code generation.");
            return;
        }

        Generator generator = new Generator();
        generator.configureRoutes(pythonResult.semanticVisitor.getRoutes());
        Map<String, Object> globals = generator.extractGlobals(pythonResult.root);

        List<PythonSemanticVisitor.TemplateRenderInfo> renders = pythonResult.semanticVisitor.getTemplateRenders();
        if (renders.isEmpty()) {
            System.out.println("No render_template(...) calls found - nothing to generate.");
            return;
        }

        try {
            Files.createDirectories(Paths.get(GENERATED_OUTPUT_DIRECTORY));
        } catch (IOException e) {
            System.out.println("Could not create " + GENERATED_OUTPUT_DIRECTORY + ": " + e.getMessage());
            return;
        }

        // A single template can have multiple render_template(...) call sites
        // (e.g. add_product.html: one for the default GET page, one for a
        // POST validation failure that also supplies "error"). Each call is
        // still validated independently above/in semantic analysis; this
        // tracking is purely about not silently overwriting the generated
        // *file* when two calls happen to resolve to genuinely different
        // compile-time contexts for the same output path.
        Map<String, List<Map<String, Object>>> generatedContextsByTemplate = new LinkedHashMap<>();

        for (PythonSemanticVisitor.TemplateRenderInfo render : renders) {
            Node templateAst = templateAsts.get(render.getTemplateName());
            if (templateAst == null) {
                System.out.println("Skipped '" + render.getTemplateName() + "': template AST not available.");
                continue;
            }
            try {
                Map<String, Object> context = generator.resolveContext(render, globals);

                List<Map<String, Object>> priorContexts =
                        generatedContextsByTemplate.computeIfAbsent(render.getTemplateName(), key -> new ArrayList<>());
                if (priorContexts.contains(context)) {
                    System.out.println("Skipped: '" + render.getTemplateName()
                            + "' already generated with an identical render_template(...) context.");
                    continue;
                }

                String html = generator.render(templateAst, context);
                Path outputPath = outputPathFor(render.getTemplateName(), priorContexts.size());
                Files.writeString(outputPath, html);
                if (priorContexts.isEmpty()) {
                    System.out.println("Generated: " + outputPath);
                } else {
                    System.out.println("Generated: " + outputPath
                            + " (render_template(...) context differs from the primary generation of '"
                            + render.getTemplateName() + "' - context keys: " + context.keySet() + ")");
                }
                priorContexts.add(context);
            } catch (CodeGenerationException e) {
                System.out.println("Code Generation: " + render.getTemplateName() + " - " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Could not write generated output for '"
                        + render.getTemplateName() + "': " + e.getMessage());
            }
        }
    }

    /**
     * The first distinct context for a template keeps its plain output name
     * (e.g. "add_product.html"); a genuinely different context for the same
     * template is written alongside it as a numbered variant rather than
     * overwriting the first, e.g. "add_product.variant2.html".
     */
    private static Path outputPathFor(String templateName, int existingVariantCount) {
        if (existingVariantCount == 0) {
            return Paths.get(GENERATED_OUTPUT_DIRECTORY, templateName);
        }
        int dot = templateName.lastIndexOf('.');
        String base = dot >= 0 ? templateName.substring(0, dot) : templateName;
        String extension = dot >= 0 ? templateName.substring(dot) : "";
        return Paths.get(GENERATED_OUTPUT_DIRECTORY, base + ".variant" + (existingVariantCount + 1) + extension);
    }

    private static PythonResult runPythonPipeline(String sourceFile, SemanticErrorReporter reporter) {
        System.out.println("[1] PYTHON / FLASK");
        System.out.println("--------------------------------------------------");
        System.out.println("Source: " + sourceFile);

        try {
            CharStream input = CharStreams.fromFileName(sourceFile);
            flaskLexer lexer = new flaskLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            flaskParser parser = new flaskParser(tokens);
            flaskParser.ProgramContext tree = parser.program();
            int syntaxErrors = parser.getNumberOfSyntaxErrors();
            System.out.println("Syntax Errors: " + syntaxErrors);

            if (syntaxErrors > 0) {
                return new PythonResult(syntaxErrors, null, null);
            }

            PythonVisitor visitor = new PythonVisitor();
            AstNode root = visitor.visit(tree);

            System.out.println();
            System.out.println("PYTHON AST:");
            System.out.println(root);

            Scope.clearReport();
            SymbolTableVisitor symbolTableVisitor = new SymbolTableVisitor();
            symbolTableVisitor.build(root);
            System.out.println();
            System.out.println("PYTHON SYMBOL TABLE:");
            Scope.printFinalReport();

            PythonSemanticVisitor semanticVisitor = new PythonSemanticVisitor(reporter, sourceFile);
            semanticVisitor.analyze(root);
            return new PythonResult(syntaxErrors, semanticVisitor, root);
        } catch (IOException e) {
            reporter.error(SemanticError.Type.INVALID_ASSIGNMENT,
                    "Unable to read Python source: " + e.getMessage(), 0, sourceFile);
            System.out.println("Syntax Errors: 1");
            System.out.println("Could not read source: " + e.getMessage());
            return new PythonResult(1, null, null);
        }
    }

    private static TemplateResult runTemplatePipeline(Path templatePath, SemanticErrorReporter reporter) {
        String sourceFile = templatePath.toString();
        String templateName = templatePath.getFileName().toString();

        System.out.println();
        System.out.println("Template: " + templateName);
        System.out.println("Source: " + sourceFile);

        try {
            String htmlCode = Files.readString(templatePath);
            htmlLexer lexer = new htmlLexer(CharStreams.fromString(htmlCode));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            htmlParser parser = new htmlParser(tokens);
            htmlParser.HtmlDocumentContext tree = parser.htmlDocument();
            int syntaxErrors = parser.getNumberOfSyntaxErrors();
            System.out.println("Syntax Errors: " + syntaxErrors);

            if (syntaxErrors > 0) {
                return new TemplateResult(syntaxErrors, null, null);
            }

            HtmlVisitor visitor = new HtmlVisitor();
            Node ast = visitor.visitHtmlDocument(tree);

            System.out.println();
            System.out.println("AST:");
            System.out.println(ast);

            WebSymbolTableVisitor webSymbolTableVisitor = new WebSymbolTableVisitor(sourceFile);
            webSymbolTableVisitor.build(ast);

            WebSemanticVisitor semanticVisitor = new WebSemanticVisitor(reporter, sourceFile);
            semanticVisitor.analyze(ast);
            return new TemplateResult(syntaxErrors, semanticVisitor, ast);
        } catch (IOException e) {
            reporter.error(SemanticError.Type.INVALID_ASSIGNMENT,
                    "Unable to read template: " + e.getMessage(), 0, sourceFile);
            System.out.println("Syntax Errors: 1");
            System.out.println("Could not read template: " + e.getMessage());
            return new TemplateResult(1, null, null);
        }
    }

    private static CssResult runCssPipeline(Path cssPath, SemanticErrorReporter reporter) {
        String sourceFile = cssPath.toString();
        String cssName = cssPath.getFileName().toString();

        System.out.println();
        System.out.println("CSS File: " + cssName);
        System.out.println("Source: " + sourceFile);

        try {
            String cssCode = Files.readString(cssPath);
            cssLexer lexer = new cssLexer(CharStreams.fromString(cssCode));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            cssParser parser = new cssParser(tokens);
            cssParser.StylesheetContext tree = parser.stylesheet();
            int syntaxErrors = parser.getNumberOfSyntaxErrors();
            System.out.println("Syntax Errors: " + syntaxErrors);

            if (syntaxErrors > 0) {
                return new CssResult(syntaxErrors, null, sourceFile);
            }

            CssVisitor visitor = new CssVisitor();
            CSS_Style ast = visitor.visitStylesheet(tree);

            System.out.println();
            System.out.println("AST:");
            System.out.println(ast);

            WebSymbolTableVisitor webSymbolTableVisitor = new WebSymbolTableVisitor(sourceFile);
            webSymbolTableVisitor.build(ast);

            return new CssResult(syntaxErrors, ast, sourceFile);
        } catch (IOException e) {
            reporter.error(SemanticError.Type.INVALID_ASSIGNMENT,
                    "Unable to read CSS source: " + e.getMessage(), 0, sourceFile);
            System.out.println("Syntax Errors: 1");
            System.out.println("Could not read CSS source: " + e.getMessage());
            return new CssResult(1, null, sourceFile);
        }
    }

    private static List<Path> discoverTemplates() {
        List<Path> templates = new ArrayList<>();
        Path directory = Paths.get(TEMPLATE_DIRECTORY);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.html")) {
            for (Path path : stream) {
                templates.add(path);
            }
        } catch (IOException e) {
            System.out.println("Could not discover templates under " + TEMPLATE_DIRECTORY + ": " + e.getMessage());
        }
        templates.sort(Comparator.comparing(path -> path.getFileName().toString()));
        return templates;
    }

    private static List<Path> discoverStandaloneCssFiles() {
        List<Path> cssFiles = new ArrayList<>();
        Path directory = Paths.get(STANDALONE_CSS_DIRECTORY);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.css")) {
            for (Path path : stream) {
                cssFiles.add(path);
            }
        } catch (IOException e) {
            System.out.println("Could not discover CSS files under " + STANDALONE_CSS_DIRECTORY + ": " + e.getMessage());
        }
        cssFiles.sort(Comparator.comparing(path -> path.getFileName().toString()));
        return cssFiles;
    }

    private static void copyDiagnostics(SemanticErrorReporter source, SemanticErrorReporter target) {
        Set<String> existing = new HashSet<>();
        for (SemanticError diagnostic : target.getErrors()) {
            existing.add(diagnostic.toString());
        }
        for (SemanticError diagnostic : source.getErrors()) {
            if (existing.add(diagnostic.toString())) {
                target.report(diagnostic);
            }
        }
    }

    private static void printSemanticSection(SemanticErrorReporter pythonReporter,
                                             SemanticErrorReporter webReporter,
                                             SemanticErrorReporter projectReporter,
                                             SemanticErrorReporter finalReporter) {
        System.out.println();
        System.out.println("[3] SEMANTIC ANALYSIS");
        System.out.println("--------------------------------------------------");

        System.out.println("Python semantic diagnostics:");
        pythonReporter.printAll();

        System.out.println();
        System.out.println("Web semantic diagnostics:");
        webReporter.printAll();

        System.out.println();
        System.out.println("Project integration diagnostics:");
        projectReporter.printAll();

        System.out.println();
        System.out.println("All semantic diagnostics:");
        finalReporter.printAll();
    }

    private static void printSummary(int pythonSyntaxErrors,
                                     int webSyntaxErrors,
                                     int cssSyntaxErrors,
                                     SemanticErrorReporter finalReporter,
                                     List<String> analyzedTemplates,
                                     List<String> analyzedCssFiles) {
        System.out.println();
        System.out.println("FINAL SUMMARY");
        System.out.println("--------------------------------------------------");
        System.out.println("Templates analyzed: " + analyzedTemplates);
        System.out.println("CSS files analyzed: " + analyzedCssFiles);
        System.out.println("Python syntax errors: " + pythonSyntaxErrors);
        System.out.println("Web syntax errors: " + webSyntaxErrors);
        System.out.println("CSS syntax errors: " + cssSyntaxErrors);
        System.out.println("Semantic errors: " + finalReporter.getErrorCount());
        System.out.println("Semantic warnings: " + finalReporter.getWarningCount());
        System.out.println();
        System.out.println("Compilation result: "
                + (pythonSyntaxErrors == 0
                && webSyntaxErrors == 0
                && cssSyntaxErrors == 0
                && !finalReporter.hasErrors()
                && finalReporter.getWarningCount() == 0
                ? "SUCCESS"
                : "COMPLETED WITH ERRORS"));
    }

    private static void printHeader() {
        System.out.println("==================================================");
        System.out.println("COMPILER2 - COMPILATION REPORT");
        System.out.println("==================================================");
        System.out.println();
    }

    private static class PythonResult {
        private final int syntaxErrors;
        private final PythonSemanticVisitor semanticVisitor;
        private final AstNode root;

        private PythonResult(int syntaxErrors, PythonSemanticVisitor semanticVisitor, AstNode root) {
            this.syntaxErrors = syntaxErrors;
            this.semanticVisitor = semanticVisitor;
            this.root = root;
        }
    }

    private static class TemplateResult {
        private final int syntaxErrors;
        private final WebSemanticVisitor semanticVisitor;
        private final Node ast;

        private TemplateResult(int syntaxErrors, WebSemanticVisitor semanticVisitor, Node ast) {
            this.syntaxErrors = syntaxErrors;
            this.semanticVisitor = semanticVisitor;
            this.ast = ast;
        }
    }

    private static class CssResult {
        private final int syntaxErrors;
        private final CSS_Style ast;
        private final String sourceFile;

        private CssResult(int syntaxErrors, CSS_Style ast, String sourceFile) {
            this.syntaxErrors = syntaxErrors;
            this.ast = ast;
            this.sourceFile = sourceFile;
        }
    }
}
