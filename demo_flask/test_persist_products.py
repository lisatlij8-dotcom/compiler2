"""Focused tests for source persistence (write_products_region / _serialize_*
helpers in app.py). Mirrors the project's existing Java test style: plain
functions, a check() pass/fail counter, no external test framework.

Uses only temporary files - never touches the real demo_flask/app.py.
"""

import ast
import os
import tempfile
from pathlib import Path

from app import (
    PRODUCTS_END_MARKER,
    PRODUCTS_START_MARKER,
    _python_string_literal,
    write_products_region,
)

passed = 0
failed = 0


def check(name, condition):
    global passed, failed
    if condition:
        passed += 1
        print("PASS: " + name)
    else:
        failed += 1
        print("FAIL: " + name)


SKELETON_BEFORE = (
    "from pathlib import Path\n\n"
    "APP_NAME = \"demo\"\n\n"
)
SKELETON_PRODUCTS = (
    "products = [\n"
    "    {\n"
    '        "id": 1,\n'
    '        "name": "placeholder",\n'
    '        "price": 1.0,\n'
    '        "category": "placeholder",\n'
    '        "description": "placeholder",\n'
    '        "image": "placeholder.jpg",\n'
    "    },\n"
    "]\n"
)
SKELETON_AFTER = (
    "\n\ndef home():\n"
    "    return APP_NAME\n"
)


def make_temp_source():
    fd, path = tempfile.mkstemp(suffix=".py", prefix="persist_products_test_")
    os.close(fd)
    content = (
        SKELETON_BEFORE
        + PRODUCTS_START_MARKER
        + "\n"
        + SKELETON_PRODUCTS
        + PRODUCTS_END_MARKER
        + SKELETON_AFTER
    )
    with open(path, "w", encoding="utf-8") as handle:
        handle.write(content)
    return path, content


def read(path):
    with open(path, "r", encoding="utf-8") as handle:
        return handle.read()


def test_add_serialization():
    path, original = make_temp_source()
    try:
        write_products_region(
            Path(path),
            [
                {
                    "id": 1,
                    "name": "sun glasses",
                    "price": 15.99,
                    "category": "glasses",
                    "description": "brown unisex glasses",
                    "image": "abc.jpg",
                },
                {
                    "id": 2,
                    "name": "Compiler Demo Product",
                    "price": 99.99,
                    "category": "demo",
                    "description": "Added from Flask UI",
                    "image": "",
                },
            ],
        )
        text = read(path)
        check(
            "add: new product's fields appear in the rewritten region",
            '"Compiler Demo Product"' in text
            and "99.99" in text
            and '"demo"' in text
            and "Added from Flask UI" in text,
        )
        check(
            "add: original product is still present alongside the new one",
            "sun glasses" in text,
        )
        check(
            "add: markers are still present exactly once each",
            text.count(PRODUCTS_START_MARKER) == 1 and text.count(PRODUCTS_END_MARKER) == 1,
        )
    finally:
        os.remove(path)


def test_edit_serialization():
    path, original = make_temp_source()
    try:
        base_product = {
            "id": 2,
            "name": "Compiler Demo Product",
            "price": 99.99,
            "category": "demo",
            "description": "Added from Flask UI",
            "image": "",
        }
        write_products_region(Path(path), [base_product])

        edited_product = dict(base_product)
        edited_product["name"] = "Compiler Demo Product Updated"
        write_products_region(Path(path), [edited_product])

        text = read(path)
        check(
            "edit: updated name is present",
            "Compiler Demo Product Updated" in text,
        )
        check(
            "edit: old name is fully gone (not just a prefix match)",
            '"Compiler Demo Product"' not in text.replace("Compiler Demo Product Updated", ""),
        )
    finally:
        os.remove(path)


def test_delete_serialization():
    path, original = make_temp_source()
    try:
        product_a = {
            "id": 1, "name": "keep-me", "price": 1.0,
            "category": "c", "description": "d", "image": "",
        }
        product_b = {
            "id": 2, "name": "delete-me", "price": 2.0,
            "category": "c", "description": "d", "image": "",
        }
        write_products_region(Path(path), [product_a, product_b])
        check("delete: both products present before delete", "delete-me" in read(path))

        write_products_region(Path(path), [product_a])
        text = read(path)
        check("delete: removed product's name no longer present", "delete-me" not in text)
        check("delete: remaining product is still present", "keep-me" in text)
    finally:
        os.remove(path)


def test_strings_with_quotes_and_apostrophes():
    # Apostrophe only - existing style ("women's belt") needs no escaping.
    apostrophe_only = _python_string_literal("women's belt")
    check(
        "apostrophe-only value stays double-quoted and unescaped",
        apostrophe_only == '"women\'s belt"',
    )

    # Double quote only, no apostrophe - falls back to single-quoted, unescaped.
    doublequote_only = _python_string_literal('He said "hi"')
    check(
        "double-quote-only value switches to single quotes, unescaped",
        doublequote_only == "'He said \"hi\"'",
    )

    # Both quote characters present - must escape, must not corrupt the file.
    both = _python_string_literal("""it's a "test" value""")
    check(
        "value with both quote types is double-quoted with '\"' escaped",
        both.startswith('"') and both.endswith('"') and '\\"' in both,
    )

    path, original = make_temp_source()
    try:
        tricky_product = {
            "id": 3,
            "name": """Tom's "Big Sale" Item""",
            "price": 5.0,
            "category": "c",
            "description": "d",
            "image": "",
        }
        write_products_region(Path(path), [tricky_product])
        text = read(path)

        check(
            "quote-containing product is written without breaking the region markers",
            text.count(PRODUCTS_START_MARKER) == 1 and text.count(PRODUCTS_END_MARKER) == 1,
        )

        # The whole file must still be valid Python syntax (a real, if
        # weaker, proxy for "did not corrupt app.py" - the compiler's own
        # flaskLexer/flaskParser acceptance of the real app.py, exercised
        # separately via a live quote-containing product through the actual
        # Flask website, is the authoritative check for grammar compatibility).
        try:
            ast.parse(text)
            parses = True
        except SyntaxError:
            parses = False
        check("file with quote-containing product remains valid Python syntax", parses)
    finally:
        os.remove(path)


def test_rest_of_file_unchanged():
    path, original = make_temp_source()
    try:
        write_products_region(
            Path(path),
            [{"id": 1, "name": "x", "price": 1.0, "category": "c", "description": "d", "image": ""}],
        )
        text = read(path)

        before_marker_index = text.index(PRODUCTS_START_MARKER)
        after_marker_index = text.index(PRODUCTS_END_MARKER) + len(PRODUCTS_END_MARKER)

        check(
            "content before PRODUCTS_START is untouched",
            text[:before_marker_index] == original[: original.index(PRODUCTS_START_MARKER)],
        )
        check(
            "content after PRODUCTS_END is untouched",
            text[after_marker_index:] == original[original.index(PRODUCTS_END_MARKER) + len(PRODUCTS_END_MARKER):],
        )
    finally:
        os.remove(path)


def test_marker_text_appearing_after_the_region_is_not_mistaken_for_the_real_marker():
    # Regression test for a real bug caught during live testing: the real
    # app.py has code AFTER "# PRODUCTS_END" (write_products_region etc.)
    # that references the marker constants by value at runtime. A naive
    # text.split(marker) with no maxsplit can match a later incidental
    # occurrence of the marker substring instead of stopping at the first
    # (real) one. This reproduces that shape - a decoy line placed AFTER
    # the real end marker, containing the marker text as a substring - and
    # verifies the real region is rewritten correctly while the decoy line
    # itself is left untouched.
    fd, path = tempfile.mkstemp(suffix=".py", prefix="persist_products_test_")
    os.close(fd)
    content = (
        SKELETON_BEFORE
        + PRODUCTS_START_MARKER
        + "\n"
        + SKELETON_PRODUCTS
        + PRODUCTS_END_MARKER
        + '\n\nDECOY = "mentions ' + PRODUCTS_START_MARKER + " and " + PRODUCTS_END_MARKER + '"\n'
    )
    with open(path, "w", encoding="utf-8") as handle:
        handle.write(content)

    try:
        write_products_region(
            Path(path),
            [{"id": 42, "name": "real-product", "price": 1.0, "category": "c", "description": "d", "image": ""}],
        )
        text = read(path)

        check(
            "the trailing decoy line is preserved verbatim, not truncated at its embedded marker text",
            text.rstrip().endswith(
                'DECOY = "mentions ' + PRODUCTS_START_MARKER + " and " + PRODUCTS_END_MARKER + '"'
            ),
        )
        check(
            "the real region was rewritten with the new product",
            "real-product" in text,
        )
        check(
            "the old placeholder product was replaced",
            "placeholder" not in text,
        )
        check(
            "each marker still appears exactly once as the real boundary "
            "(plus, for the start marker, its one incidental mention inside the decoy)",
            text.count(PRODUCTS_END_MARKER) == 2 and text.count(PRODUCTS_START_MARKER) == 2,
        )
    finally:
        os.remove(path)


def main():
    test_add_serialization()
    test_edit_serialization()
    test_delete_serialization()
    test_strings_with_quotes_and_apostrophes()
    test_rest_of_file_unchanged()
    test_marker_text_appearing_after_the_region_is_not_mistaken_for_the_real_marker()

    print()
    print(str(passed) + " passed, " + str(failed) + " failed")
    if failed > 0:
        raise SystemExit(1)


if __name__ == "__main__":
    main()
