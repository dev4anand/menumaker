 function showContent(type) {
      // Hide both divs initially
      document.getElementById('userContent').style.display = 'none';
      document.getElementById('restaurantContent').style.display = 'none';

      // Show the relevant div
      if (type === 'user') {
        document.getElementById('userContent').style.display = 'block';
      } else if (type === 'restaurant') {
        document.getElementById('restaurantContent').style.display = 'block';
      }
    }
// function deleteUser(userId, button) {
// alert(userId);
//        if (confirm("Are you sure you want to delete this user?")) {
//            fetch(`/delete-user/${userId}`, {
//                method: 'POST',
//                headers: {
//                    'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content') // CSRF Token for Spring Security
//                }
//            })
//            .then(response => {
//                if (response.ok) {
//                    // Remove the row from the table
//                    const row = button.closest('tr');
//                    row.parentNode.removeChild(row);
//                    alert("User deleted successfully.");
//                } else {
//                    alert("Failed to delete user.");
//                }
//            })
//            .catch(error => {
//                console.error("Error:", error);
//                alert("An error occurred.");
//            });
//        }
//    }
