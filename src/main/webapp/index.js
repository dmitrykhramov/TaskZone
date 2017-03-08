/**
 * Created by Dmitry on 04.03.2017.
 */

jQuery(document).ready(function($) {

    var users;

    $.ajax({
        type: 'GET',
        url: '/rest/tasks/all'

    }).done(function (data, textStatus, jqXHR) {

        var csrfToken = jqXHR.getResponseHeader('X-CSRF-TOKEN');
        if (csrfToken) {
            var cookie = JSON.parse($.cookie('taskzone'));
            cookie.csrf = csrfToken;
            $.cookie('taskzone', JSON.stringify(cookie));
        }

        // Hiding buttons for employees
        $.ajax({
            url: '/rest/user/checkuser',
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'GET',
            success: function (isManager) {
                if (!isManager) {
                    $("#historyTab").css("display", "none");
                    $("#navBtnAddTask").css("display", "none");
                }
            }
        });

        // Get all users
        $.ajax({
            url: '/rest/user/list',
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'GET',
            success: function (data) {
                users = data;
                console.log(users);
                $.each(data, function (i, item) {
                    $('#assigneeAdd').append($('<option>', {
                        value: item.userId,
                        text : item.firstName + ' ' + item.lastName
                    }));
                    $('#assignee').append($('<option>', {
                        value: item.userId,
                        text : item.firstName + ' ' + item.lastName
                    }));
                });
            }
        });
        console.log(data);
        for (var i = 0; i < data.length; i++ ) {
            var date = new Date(data[i].deadline).toLocaleString().split(',')[0];
            var div =
                $('<div id="' + data[i].id +'" class="card"><h3>' + data[i].title + '</h3>' +
            '<p class="card-text" style="display:inline;">' + data[i].assignee.firstName + ' ' + data[i].assignee.lastName +'</p>' +
            '<p>Due date: ' + date + '</p>' +
            '<button type="button" class="btn btn-success btn-circle btn-lg" id="submitTaskPublic"><i class="glyphicon glyphicon-ok"></i></button></div>');
            $("#publictasks").append(div);
        }

    }).fail(function (jqXHR, textStatus, errorThrown) {

        if (jqXHR.status === 401) { // HTTP Status 401: Unauthorized
            var cookie = JSON.stringify({method: 'GET', url: '/', csrf: jqXHR.getResponseHeader('X-CSRF-TOKEN')});
            $.cookie('taskzone', cookie);

            window.location = '/login.html';

        } else {
            console.error('Some problem!');
        }
    });


    // Logout function
    $('#logoutButton').on('click', function (event) {
        event.preventDefault();

        var cookie = JSON.parse($.cookie('taskzone'));
        $.ajax({
            data: {},
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            timeout: 1000,
            type: 'POST',
            url: '/logout'

        }).done(function(data, textStatus, jqXHR) {

            console.info('logged out!');
            window.location = '/';

        }).fail(function(jqXHR, textStatus, errorThrown) {

            console.error('Logout does not work');
        });
    });


    // Add new task by manager
    $("#addTaskBtn").click(function (event) {
        event.preventDefault();
        var data = {};
        var index = $("#assigneeAdd").val() - 1;
        data["title"] = $("#titleAdd").val();
        data["description"] = $("#descriptionAdd").val();
        data["deadline"] = $("#deadlineAdd").val();
        data["assignee"] = {
            email: users[index].email,
            firstName: users[index].firstName,
            lastName: users[index].lastName,
            manager: users[index].manager,
            password: users[index].password,
            userId: users[index].userId,
            username: users[index].username
        };
        console.log(data);

        var cookie = JSON.parse($.cookie('taskzone'));

        $.ajax({
            url: 'rest/tasks/add',
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (response) {
                refresh();
                $("#titleAdd").val('');
                $("#descriptionAdd").val('');
                $("#deadlineAdd").val('');
            }
        });
    });

    // Add new private task
    $("#addTaskPrivateBtn").click(function (event) {
        event.preventDefault();
        var data = {};
        data["title"] = $("#titleAddPrivate").val();
        data["description"] = $("#descriptionAddPrivate").val();
        data["deadline"] = $("#deadlineAddPrivate").val();

        var cookie = JSON.parse($.cookie('taskzone'));

        $.ajax({
            url: 'rest/tasks/private/add',
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (response) {
                $("#titleAddPrivate").val('');
                $("#descriptionAddPrivate").val('');
                $("#deadlineAddPrivate").val('');
                refreshPrivate();
            }
        });
    });


    // Open the task card by click
    var clickedDiv;
    $("#publictasks").on("click", ".card", function() {
        clickedDiv = $(this).attr('id');

        var cookie = JSON.parse($.cookie('taskzone'));

        $.ajax({
            url: 'rest/tasks/edit/' + clickedDiv,
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'GET',
            dataType: 'json',
            contentType: 'application/json',
            data: {},
            success: function (response) {
                $('#title').val(response.title);
                $('#idUpdate').val(response.id);
                $('#deadline').val(response.deadline);
                $('#description').val(response.description);
                $('#assignee').val(response.assignee.userId);
                $('#task').modal('show');
            }
        });
        return false;

    });

    // Open the private task card by click
    $("#privatetasks").on("click", ".card", function() {
        clickedDiv = $(this).attr('id');

        var cookie = JSON.parse($.cookie('taskzone'));

        $.ajax({
            url: 'rest/tasks/private/edit/' + clickedDiv,
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'GET',
            dataType: 'json',
            contentType: 'application/json',
            data: {},
            success: function (response) {
                $('#titlePrivate').val(response.title);
                $('#idUpdatePrivate').val(response.id);
                $('#deadlinePrivate').val(response.deadline);
                $('#descriptionPrivate').val(response.description);
                $('#taskPrivate').modal('show');
            }
        });
        return false;

    });


    // Update existing task by manager
    $("#saveTaskBtn").click(function (event) {
        event.preventDefault();
        var cookie = JSON.parse($.cookie('taskzone'));

        var data = {};
        var index = $("#assignee").val() - 1;
        data["title"] = $("#title").val();
        data["id"] = $("#idUpdate").val();
        data["description"] = $("#description").val();
        data["deadline"] = $("#deadline").val();
        data["assignee"] = {
            email: users[index].email,
            firstName: users[index].firstName,
            lastName: users[index].lastName,
            manager: users[index].manager,
            password: users[index].password,
            userId: users[index].userId,
            username: users[index].username
        };

        $.ajax({
            url: 'rest/tasks/update',
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'PUT',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (response) {
                refresh();
            }
        });
    });

    // Update private task
    $("#savePrivateTaskBtn").click(function (event) {
        event.preventDefault();
        var cookie = JSON.parse($.cookie('taskzone'));

        var data = {};
        data["title"] = $("#titlePrivate").val();
        data["id"] = $("#idUpdatePrivate").val();
        data["description"] = $("#descriptionPrivate").val();
        data["deadline"] = $("#deadlinePrivate").val();

        $.ajax({
            url: 'rest/tasks/private/update',
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'PUT',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (response) {
                refreshPrivate();
            }
        });
    });


    // Delete task by manager
    $("#deleteTaskBtn").click(function (event) {
        event.preventDefault();
        var cookie = JSON.parse($.cookie('taskzone'));

        $.ajax({
            url: 'rest/tasks/remove/' + clickedDiv,
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'DELETE',
            success: function (response) {
                refresh();
            }
        });
    });

    // Delete private task
    $("#deletePrivateTaskBtn").click(function (event) {
        event.preventDefault();
        var cookie = JSON.parse($.cookie('taskzone'));

        $.ajax({
            url: 'rest/tasks/private/remove/' + clickedDiv,
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'DELETE',
            success: function (response) {
                refreshPrivate();
            }
        });
    });


    // Checked the task is done
    $("#publictasks").on("click", "button", function() {

        var id = $(this).parent().attr('id');

        var cookie = JSON.parse($.cookie('taskzone'));

        $.ajax({
            url: 'rest/tasks/toogle/' + id,
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'GET',
            success: function (response) {
                refresh();
            }
        });
        return false;

    });

    // Checked the task is done
    $("#privatetasks").on("click", "button", function() {

        var id = $(this).parent().attr('id');

        var cookie = JSON.parse($.cookie('taskzone'));

        $.ajax({
            url: 'rest/tasks/private/toogle/' + id,
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'GET',
            success: function (response) {
                refreshPrivate();
            }
        });
        return false;

    });



    // Open public tasks tab
    $("#publicTab").click(function (event) {
        event.preventDefault();
        var cookie = JSON.parse($.cookie('taskzone'));

        $("#publicTab").tab('show');
        $("#privatetasks").hide();
        $("#historytasks").hide();
        $("#publictasks").show();

        $.ajax({
            url: 'rest/tasks/all',
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            dataType: 'json',
            contentType: 'application/json',
            type: 'GET',
            success: function (data) {
                $("#publictasks").children().remove();
                for (var i = 0; i < data.length; i++) {
                    var date = new Date(data[i].deadline).toLocaleString().split(',')[0];
                    var div =
                        $('<div id="' + data[i].id +'" class="card"><h3>' + data[i].title + '</h3>' +
                            '<p class="card-text" style="display:inline;">' + data[i].assignee.firstName + ' ' + data[i].assignee.lastName +'</p>' +
                            '<p>Due date: ' + date + '</p>' +
                            '<button type="button" class="btn btn-success btn-circle btn-lg" id="submitTaskPublic"><i class="glyphicon glyphicon-ok"></i></button></div>');
                    $("#publictasks").append(div);
                }
            }
        });
    });


    // Open private tasks tab
    $("#privateTab").click(function (event) {
        event.preventDefault();
        var cookie = JSON.parse($.cookie('taskzone'));

        $("#privateTab").tab('show');
        $("#publictasks").hide();
        $("#historytasks").hide();
        $("#privatetasks").show();

        $.ajax({
            url: 'rest/tasks/private/all',
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            dataType: 'json',
            contentType: 'application/json',
            type: 'GET',
            success: function (data) {
                $("#privatetasks").children().remove();
                for (var i = 0; i < data.length; i++) {
                    var date = new Date(data[i].deadline).toLocaleString().split(',')[0];
                    var div =
                        $('<div id="' + data[i].id +'" class="card"><h3>' + data[i].title + '</h3>' +
                            '<p>Due date: ' + date + '</p>' +
                            '<button type="button" class="btn btn-success btn-circle btn-lg" id="submitTaskPrivate"><i class="glyphicon glyphicon-ok"></i></button></div>');
                    $("#privatetasks").append(div);
                }
            }
        });
    });

    // Open history
    $("#historyTab").click(function (event) {
        event.preventDefault();
        var cookie = JSON.parse($.cookie('taskzone'));

        $("#historyTab").tab('show');
        $("#publictasks").hide();
        $("#privatetasks").hide();
        $("#historytasks").show();

        $.ajax({
            url: 'rest/tasks/history',
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            dataType: 'json',
            contentType: 'application/json',
            type: 'GET',
            success: function (data) {
                $("#historytasks").children().remove();
                for (var i = 0; i < data.length; i++) {
                    var div =
                        $('<div id="' + data[i].id +'" class="card"><h3>' + data[i].title + '</h3>' +
                            '<p class="card-text" style="display:inline;">' + data[i].assignee +'</p>' +
                            '<p>Due date: ' + data[i].deadline + '</p>' +
                            '</div>');
                    $("#historytasks").append(div);
                }
            }
        });
    });


    // AJAX reload page after manipulating with tasks
    function refresh() {
        var cookie = JSON.parse($.cookie('taskzone'));
        $.ajax({
            url: 'rest/tasks/all',
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'GET',
            success: function (data) {
                $(".card").remove();
                for (var i = 0; i < data.length; i++ ) {
                    var date = new Date(data[i].deadline).toLocaleString().split(',')[0];
                    var div =
                        $('<div id="' + data[i].id +'" class="card"><h3>' + data[i].title + '</h3>' +
                            '<p class="card-text" style="display:inline;">' + data[i].assignee.firstName + ' ' + data[i].assignee.lastName +'</p>' +
                            '<p>Due date: ' + date + '</p>' +
                            '<button type="button" class="btn btn-success btn-circle btn-lg"><i class="glyphicon glyphicon-ok"></i></button></div>');
                    $("#publictasks").append(div);
                }
            }
        });
    }

    // AJAX reload page after manipulating with tasks
        function refreshPrivate() {
            var cookie = JSON.parse($.cookie('taskzone'));
            $.ajax({
                url: 'rest/tasks/private/all',
                headers: {'X-CSRF-TOKEN': cookie.csrf},
                type: 'GET',
                success: function (data) {
                    $("#privatetasks").children().remove();
                    for (var i = 0; i < data.length; i++ ) {
                        var date = new Date(data[i].deadline).toLocaleString().split(',')[0];
                        var div =
                            $('<div id="' + data[i].id +'" class="card"><h3>' + data[i].title + '</h3>' +
                                '<p>Due date: ' + date+ '</p>' +
                                '<button type="button" class="btn btn-success btn-circle btn-lg" id="submitTaskPrivate"><i class="glyphicon glyphicon-ok"></i></button></div>');
                        $("#privatetasks").append(div);
                    }
                }
            });
        }

});