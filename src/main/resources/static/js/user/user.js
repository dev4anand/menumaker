//function showContent(type) {
//    // Hide both divs initially
//    document.getElementById('userContent').style.display = 'none';
//    document.getElementById('restaurantContent').style.display = 'none';
//
//    // Show the relevant div
//    if (type === 'user') {
//        document.getElementById('userContent').style.display = 'block';
//    } else if (type === 'restaurant') {
//        document.getElementById('restaurantContent').style.display = 'block';
//    }
//}


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
    //            success: function (data) {
    //                toastr.success(
    //                    "Successfuly deleted!.",
    //                    {
    //                        positionClass: "toastr toast-top-center",
    //                        containerId: "toast-top-center",
    //                    }
    //                );
    //            },
    //            error: function () {
    //                toastr.error(
    //                    "An error occurred while deleting the user!.",
    //                    {
    //                        positionClass: "toastr toast-top-center",
    //                        containerId: "toast-top-center",
    //                    }
    //                );
    //            }
    //        });
    //    });



    $('.edit-btn').on('click', function (event) {
        event.preventDefault();
        const userId = $(this).data('id');

        $.ajax({
            url: `/edit-user/${userId}`,
            type: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
            },
            success: function (data) {
                console.log(data);
                if (data) {
                    $('#editShopForm').data('id', data.gid).attr('data-id', data.gid);
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
        const userId = $(this).data('id') || $(this).attr('data-id');
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
