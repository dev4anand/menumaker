
    $("#storeForm").on("submit", function (e) {
        e.preventDefault(); // Prevent default form submission

        var actionUrl = $('#storeForm').attr("action");
        var formData = new FormData(this);
       /* var token = $("input[name='_csrf']").val();*/
        //const actionUrl = $(this).attr("action"); // Get form action URL

        $.ajax({
            url: actionUrl,
            type: 'POST',
            data: formData,
             processData: false, // Prevent jQuery from processing the data
                contentType: false, // Prevent jQuery from setting a default Content-Type header
               /*  headers: {
                        'X-CSRF-TOKEN': token // Include CSRF token in the request headers
                    },*/
            success: function (response) {
                    if (isJSONObj(response)) {
              					var obj = JSON.parse(response);
               					if (obj.status == 'F') {
               						displayToastrMessage(obj.message,'e');
               					}
               					else if (obj.status == 'T') {
               						$('#kt_modal_add_store').modal('toggle');
               						$('#storeListPartPage').html(response);
               						displayToastrMessage('Store saved successfully!', 's')
               					}
               				}
               				else {
               					$('#kt_modal_add_store').modal('toggle');
               					$('#storeListPartPage').html(response);
               					displayToastrMessage('Store saved successfully!', 's')
               				}
            },
            error: function (error) {
                // Handle error response
                alert("An error occurred while saving the store.");
            }
        });
    });

