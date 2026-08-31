const imageInput = document.getElementById('image');
const imagePreview = document.getElementById('image-preview');
const imagePlaceholder = document.getElementById('image-placeholder');

imageInput.addEventListener('change', () => {
  const file = imageInput.files[0];
  if (!file) {
    return;
  }

  imagePreview.src = URL.createObjectURL(file);
  imagePreview.style.display = 'grid';
  if (imagePlaceholder) {
    imagePlaceholder.style.display = 'none';
  }
});
