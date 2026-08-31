const imageInput = document.getElementById('image');
const imagePreview = document.getElementById('image-preview');
const imageTools = document.getElementById('image-tools');
const changeImage = document.getElementById('change-image');
const removeImage = document.getElementById('remove-image');
let currentPreviewUrl = null;

function clearSelectedImage() {
  imageInput.value = '';
  if (currentPreviewUrl) {
    URL.revokeObjectURL(currentPreviewUrl);
    currentPreviewUrl = null;
  }
    imagePreview.removeAttribute('src');
    imagePreview.style.display = 'none';
  imageTools.style.display = 'none';
}

function showSelectedImage(file) {
  if (currentPreviewUrl) {
    URL.revokeObjectURL(currentPreviewUrl);
  }
  currentPreviewUrl = URL.createObjectURL(file);
  imagePreview.src = currentPreviewUrl;
  imagePreview.style.display = 'block';
  imageTools.style.display = 'flex';
}

imageInput.addEventListener('change', () => {
  const file = imageInput.files[0];
  if (!file) {
    clearSelectedImage();
    return;
  }

  showSelectedImage(file);
});

changeImage.addEventListener('click', () => {
  imageInput.click();
});

removeImage.addEventListener('click', clearSelectedImage);
