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


$(document).ready(function () {
    $('.delete-btn').on('click', function (event) {
        event.preventDefault();
        const userId = $(this).data('id');

        $.ajax({
            url: `/delete-user/${userId}`,
            type: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
            },
            success: function (data) {
                showAlert(data.message, data.success ? 'success' : 'danger');
                if (data.success) {
                    setTimeout(function () {
                        location.reload();
                    }, 1000);
                }
            },
            error: function () {
                showAlert('An error occurred while deleting the user.', 'danger');
            }
        });
    });
$("#createUserForm").on("submit", function (e) {
        e.preventDefault();
    console.log('123');
        // Collect form data
        const formData = {
            email: $("#createuseremail").val(),
            mobile: $("#createusermobile").val(),
            password: $("#creteuserpassword").val(),
        };

        // Send AJAX request
        $.ajax({
            type: "POST",
            url: "/createusers",
            contentType: "application/json",
            data: JSON.stringify(formData),
            success: function (data) {
                showAlert(data.message, data.success ? 'success' : 'danger');
                if (data.success) {
                    setTimeout(function () {
                        location.reload();
                    }, 1000); /
                }
            },
            error: function () {
                showAlert('An error occurred while deleting the user.', 'danger');
            },
        });
    });
    function showAlert(message, type) {
        const $alertMessageElement = $('#alert-message');
        $alertMessageElement
            .text(message)
            .removeClass()
            .addClass(`alert alert-${type}`)
            .show();
    }
});
