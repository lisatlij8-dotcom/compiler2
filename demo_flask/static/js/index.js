const deleteModal = document.getElementById('delete-modal');
const deleteMessage = document.getElementById('delete-modal-message');
const cancelDelete = document.getElementById('cancel-delete');
const confirmDelete = document.getElementById('confirm-delete');
let selectedDeleteForm = null;
let isSubmittingDelete = false;

function openDeleteModal(formId, productName) {
  selectedDeleteForm = document.getElementById(formId);
  isSubmittingDelete = false;
  confirmDelete.disabled = false;
  deleteMessage.textContent = `Are you sure you want to delete ${productName}? This action cannot be undone.`;
  deleteModal.classList.add('open');
  deleteModal.setAttribute('aria-hidden', 'false');
  cancelDelete.focus();
}

function closeDeleteModal() {
  deleteModal.classList.remove('open');
  deleteModal.setAttribute('aria-hidden', 'true');
  selectedDeleteForm = null;
  isSubmittingDelete = false;
  confirmDelete.disabled = false;
}

document.querySelectorAll('.delete-trigger').forEach((button) => {
  button.addEventListener('click', () => {
    openDeleteModal(button.dataset.formId, button.dataset.productName);
  });
});

cancelDelete.addEventListener('click', closeDeleteModal);

deleteModal.addEventListener('click', (event) => {
  if (event.target === deleteModal) {
    closeDeleteModal();
  }
});

document.addEventListener('keydown', (event) => {
  if (event.key === 'Escape' && deleteModal.classList.contains('open')) {
    closeDeleteModal();
  }
});

confirmDelete.addEventListener('click', () => {
  if (!selectedDeleteForm || isSubmittingDelete) {
    return;
  }
  isSubmittingDelete = true;
  confirmDelete.disabled = true;
  selectedDeleteForm.submit();
});
