console.log('Ready to go!');
// Borramos notificaciones
document.addEventListener('DOMContentLoaded', () => {
    (document.querySelectorAll('.notification .info') || []).forEach(($delete) => {
        const $notification = $delete.parentNode;

        $delete.addEventListener('click', () => {
            $notification.parentNode.removeChild($notification);
        });
    });
});