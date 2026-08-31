from pathlib import Path
from uuid import uuid4

from flask import Flask, redirect, render_template, request, url_for
from werkzeug.utils import secure_filename

BASE_DIR = Path(__file__).resolve().parent
UPLOAD_FOLDER = BASE_DIR / "static" / "uploads"
ALLOWED_IMAGE_EXTENSIONS = ["png", "jpg", "jpeg", "webp"]

app = Flask(
    __name__,
    template_folder=str(BASE_DIR / "templates"),
    static_folder=str(BASE_DIR / "static"),
)
app.config["UPLOAD_FOLDER"] = UPLOAD_FOLDER
app.config["MAX_CONTENT_LENGTH"] = 5 * 1024 * 1024
UPLOAD_FOLDER.mkdir(parents=True, exist_ok=True)

# Built with "+" so this line's own text never spells out the marker
# comment below verbatim - see write_products_region for why that matters.
PRODUCTS_START_MARKER = "# PRODUCTS" + "_START"
PRODUCTS_END_MARKER = "# PRODUCTS" + "_END"

# PRODUCTS_START
products = [
    {
        "id": 1,
        "name": "sun glasses",
        "price": 15.99,
        "category": "glasses",
        "description": "brown unisex glasses",
        "image": "3ff72706e31e4106b83fcba44d51dcae.jpg",
    },
    {
        "id": 4,
        "name": "belt",
        "price": 40.0,
        "category": "belt",
        "description": "women's belt",
        "image": "a2b771a6ad0a45e2a8a49a549674ccb0.jpg",
    },
    {
        "id": 5,
        "name": "miu miu handbag",
        "price": 150.0,
        "category": "handbag",
        "description": "luxury women's handbag",
        "image": "5b5c9432ca49415fa56d18b9f02d61c5.png",
    },
    {
        "id": 6,
        "name": "heels",
        "price": 180.0,
        "category": "heels",
        "description": "fjvjngfhjbj",
        "image": "e33b4248519f4284b2a01373d0869da7.png",
    },
]
# PRODUCTS_END


# The helpers below only use syntax the compiler's Flask grammar subset
# actually supports: no ternary expressions, no "in"/"not in" boolean
# operator, no default parameter values, no "%" formatting, no slicing, no
# triple-quoted docstrings. String methods like .replace/.split/.count are
# plain method calls, so the grammar's argument/trailer rules cover them
# regardless of what those methods do at runtime.

def _python_string_literal(value):
    # Serializes value as a Python string literal the flaskLexer STRING
    # token accepts. Prefers an unescaped double-quoted literal (matching
    # the existing source style); switches to single quotes, or escapes,
    # only when the value's own content actually requires it - so ordinary
    # apostrophes (e.g. "women's belt") never need an escape.
    text = ""
    if value != None:
        text = str(value)

    has_double_quote = text.count('"') > 0
    has_single_quote = text.count("'") > 0

    quote = '"'
    if has_double_quote and not has_single_quote:
        quote = "'"

    escaped = text.replace("\\", "\\\\")
    escaped = escaped.replace(quote, "\\" + quote)
    escaped = escaped.replace("\n", "\\n")
    escaped = escaped.replace("\r", "\\r")
    escaped = escaped.replace("\t", "\\t")

    return quote + escaped + quote


def _serialize_product_id(value):
    return str(int(value))


def _serialize_product_price(value):
    # str() of a float already produces the shortest round-tripping decimal
    # form (e.g. "15.99", "40.0"), matching the existing literal's style.
    return str(float(value))


def _serialize_products_literal(item_list):
    lines = ["products = ["]
    for product in item_list:
        lines.append("    {")
        lines.append('        "id": ' + _serialize_product_id(product["id"]) + ",")
        lines.append('        "name": ' + _python_string_literal(product.get("name", "")) + ",")
        lines.append('        "price": ' + _serialize_product_price(product.get("price", 0)) + ",")
        lines.append('        "category": ' + _python_string_literal(product.get("category", "")) + ",")
        lines.append('        "description": ' + _python_string_literal(product.get("description", "")) + ",")
        lines.append('        "image": ' + _python_string_literal(product.get("image", "")) + ",")
        lines.append("    },")
    lines.append("]")
    return "\n".join(lines)


def write_products_region(path, item_list):
    # Rewrites only the products list literal between the two region
    # marker comments, preserving everything else in the file byte-for-
    # byte. Takes an explicit path/list (no defaults - the grammar has
    # none) so tests can target a temporary file instead of the real
    # app.py. maxsplit=1 on both splits means only the FIRST occurrence of
    # each marker constant's value is treated as the real boundary, so a
    # later incidental occurrence (this function referencing the marker
    # constants by NAME, not value, does not create one - but a stray
    # comment spelling the marker text out verbatim would) is never
    # mistaken for it. That is also why nothing in this file, including
    # comments, ever spells the two marker comments out literally except
    # the two real marker lines themselves.
    text = path.read_text(encoding="utf-8")

    before_and_rest = text.split(PRODUCTS_START_MARKER, 1)
    before = before_and_rest[0]
    rest = before_and_rest[1]

    old_region_and_after = rest.split(PRODUCTS_END_MARKER, 1)
    after = old_region_and_after[1]

    new_text = (
        before
        + PRODUCTS_START_MARKER
        + "\n"
        + _serialize_products_literal(item_list)
        + "\n"
        + PRODUCTS_END_MARKER
        + after
    )

    path.write_text(new_text, encoding="utf-8")


def persist_products_to_source():
    # The compiler reads demo_flask/app.py as its real source input, so
    # CRUD changes made through the website must land here too.
    write_products_region(Path(__file__), products)


def find_product(product_id):
    for product in products:
        if product["id"] == product_id:
            return product
    return None


def next_product_id():
    max_id = 0
    for product in products:
        if product["id"] > max_id:
            max_id = product["id"]
    return max_id + 1


def allowed_image(filename):
    parts = filename.rsplit(".", 1)
    if len(parts) != 2:
        return False
    extension = parts[1].lower()
    for allowed in ALLOWED_IMAGE_EXTENSIONS:
        if extension == allowed:
            return True
    return False


def save_product_image(file_storage):
    if not file_storage or file_storage.filename == "":
        return None
    if not allowed_image(file_storage.filename):
        return None

    original_name = secure_filename(file_storage.filename)
    parts = original_name.rsplit(".", 1)
    extension = parts[1].lower()
    filename = uuid4().hex + "." + extension
    file_storage.save(app.config["UPLOAD_FOLDER"] / filename)
    return filename


def delete_product_image(product):
    image = product.get("image")
    if not image:
        return
    image_path = app.config["UPLOAD_FOLDER"] / image
    image_path.unlink(missing_ok=True)


@app.route("/")
def home():
    return render_template("index.html", products=products)


@app.route("/add", methods=["GET", "POST"])
def add_product():
    if request.method == "POST":
        image_file = request.files.get("image")
        if image_file and image_file.filename and not allowed_image(image_file.filename):
            return render_template(
                "add_product.html",
                error="Please upload a PNG, JPG, JPEG, or WEBP image.",
            ), 400

        image = save_product_image(image_file)
        new_product = {
            "id": next_product_id(),
            "name": request.form.get("name", "").strip(),
            "price": float(request.form.get("price") or 0),
            "category": request.form.get("category", "").strip(),
            "description": request.form.get("description", "").strip(),
            "image": image or "",
        }
        products.append(new_product)
        persist_products_to_source()
        return redirect(url_for("home"))

    return render_template("add_product.html")


@app.route("/product/<int:product_id>")
def product_details(product_id):
    product = find_product(product_id)
    if product == None:
        return redirect(url_for("home"))
    return render_template("product_details.html", product=product)


@app.route("/edit/<int:product_id>", methods=["GET", "POST"])
def edit_product(product_id):
    product = find_product(product_id)
    if product == None:
        return redirect(url_for("home"))

    if request.method == "POST":
        image_file = request.files.get("image")
        if image_file and image_file.filename and not allowed_image(image_file.filename):
            return render_template(
                "edit_product.html",
                product=product,
                error="Please upload a PNG, JPG, JPEG, or WEBP image.",
            ), 400

        new_image = save_product_image(image_file)
        old_image = product.get("image")

        product["name"] = request.form.get("name", "").strip()
        product["price"] = float(request.form.get("price") or 0)
        product["category"] = request.form.get("category", "").strip()
        product["description"] = request.form.get("description", "").strip()

        if new_image:
            product["image"] = new_image
            if old_image:
                delete_product_image({"image": old_image})

        persist_products_to_source()
        return redirect(url_for("product_details", product_id=product["id"]))

    return render_template("edit_product.html", product=product)


@app.route("/delete", methods=["POST"])
def delete_product():
    product_id = int(request.form.get("product_id") or 0)
    product = find_product(product_id)
    if product != None:
        delete_product_image(product)
        products.remove(product)
        persist_products_to_source()
    return redirect(url_for("home"))


if __name__ == "__main__":
    # use_reloader=False: app.py rewrites its own products literal on every
    # CRUD request, and the reloader watches app.py for changes - without
    # this it would restart the worker process mid-request.
    app.run(host="127.0.0.1", port=5000, debug=True, use_reloader=False)
