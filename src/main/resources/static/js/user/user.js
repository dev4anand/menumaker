
$(document).ready(function () {
    // $('.delete-btn').on('click', function (event) {
    //     event.preventDefault();
    //     let userId = $(this).data('gid');

    //     $.ajax({
    //         url: `/delete-user/${userId}`,
    //         type: 'POST',
    //         headers: {
    //             'Content-Type': 'application/json',
    //             'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
    //         },
    //         success: function (data) {
    //             showAlert(data.message, data.success ? 'success' : 'danger');
    //             if (data.success) {
    //                 setTimeout(function () {
    //                     location.reload();
    //                 }, 1000);
    //             }
    //         },
    //         error: function () {
    //             showAlert('An error occurred while deleting the user.', 'danger');
    //         }
    //     });
    // });
    $('.delete-btn').on('click', function (event) {
        event.preventDefault();
        let userId = $(this).data('gid');

        // Show SweetAlert confirmation dialog before proceeding
        Swal.fire({
            title: "Are you sure?",
            text: "You won't be able to revert this!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Yes, delete it!",
        }).then((result) => {
            // If user confirms, proceed with the delete action
            if (result.isConfirmed) {
                // Show loading SweetAlert before the AJAX call starts
                Swal.fire({
                    title: 'Deleting user...',
                    text: 'Please wait while we delete the user.',
                    didOpen: () => {
                        Swal.showLoading();
                    },
                    allowOutsideClick: false,
                    showConfirmButton: false,
                });

                // AJAX request to delete the user
                $.ajax({
                    url: `/delete-user/${userId}`,
                    type: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
                    },
                    success: function (data) {
                        // Show success or error SweetAlert message after AJAX call
                        Swal.fire({
                            title: data.success ? 'Success!' : 'Error!',
                            text: data.message,
                            icon: data.success ? 'success' : 'error',
                            timer: 2000, // Auto-close after 2 seconds
                            didClose: () => {
                                if (data.success) {
                                    setTimeout(function () {
                                        location.reload();
                                    }, 1000);
                                }
                            }
                        });
                    },
                    error: function () {
                        // Show error message if AJAX fails
                        Swal.fire({
                            title: 'Error!',
                            text: 'An error occurred while deleting the user.',
                            icon: 'error',
                            timer: 2000, // Auto-close after 2 seconds
                        });
                    }
                });
            }
        });
    });

    
    $('.edit-btn').on('click', function (event) {
        event.preventDefault();
        let userGid = $(this).data('gid');
        $.ajax({
            url: `/edit-user/${userGid}`,
            type: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
            },
            success: function (data) {
                console.log(data);
                if (data) {
                    $('#editShopForm').data('gid', data.gid).attr('data-gid', data.gid);
                    $("#editusermobile").val(data.user.mobile);
                    $("#edituseremail").val(data.user.email);
                    $("#editshopname").val(data.shop.name);
                    $("#editshopaddress").val(data.shop.address);
                    $("#edituserpassword").val(null);
                }
            },
            error: function () {
                showAlert('An error occurred while fetching the user details.', 'danger');

            }
        });
    });

    $("#createShopForm").on("submit", function (e) {
        e.preventDefault();
        const formData = {
            password: $("#creteuserpassword").val(),
            email: $("#createuseremail").val(),
            mobile: $("#createusermobile").val(),
            shopAddress: $("#createshopaddress").val(),
            shopName: $("#createshopname").val(),
        };
        // Send AJAX request
        $.ajax({
            type: "POST",
            url: "/CreateRestaurant",
            contentType: "application/json",
            data: JSON.stringify(formData),
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
            },
        });
    });



    $("#editShopForm").on("submit", function (e) {
        e.preventDefault();
        const formData = {
            email: $("#edituseremail").val(),
            shopName: $("#editshopname").val(),
            shopAddress: $("#editshopaddress").val(),
            mobile: $("#editusermobile").val(),
            password: $("#edituserpassword").val(),
        };
        let userId = $(this).data('gid') || $(this).attr('data-gid');
        // Send AJAX request
        $.ajax({
            type: "POST",
            url: `/UpdateUser/${userId}`,
            contentType: "application/json",
            data: JSON.stringify(formData),
            success: function (data) {
                showAlert(data.message, data.success ? 'success' : 'danger');
                if (data.success) {
                    setTimeout(function () {
                        location.reload();
                    }, 1000);
                }
            },
            error: function () {
                showAlert('An error occurred while editing the user.', 'danger');
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
