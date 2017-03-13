/**
 * Created by Dmitry on 04.03.2017.
 */

jQuery(document).ready(function($) {

    var users;
    var currentUser;

    // Initial request
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

        // User settings
        $.ajax({
            url: '/rest/user/principal',
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'GET',
            success: function (user) {
                currentUser = user;
                if (!currentUser.manager) {
                    $("#historyTab").css("display", "none");
                    $("#navBtnAddTask").css("display", "none");
                    $("#title").attr('disabled', 'disabled');
                    $("#deadline").attr('disabled', 'disabled');
                    $("#assignee").attr('disabled', 'disabled');
                    $("#description").attr('disabled', 'disabled');
                    $("#deleteTaskBtn").hide();
                    $("#manageUsersLink").hide();
                }
                // Data for profile page
                updateUserProfile(currentUser);

            }
        });

        loadPublicTasks();
        loadLastHistory();
        loadPrivateTasks();
        loadHistory();
        loadUsers();

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
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'POST',
            url: '/logout',
            success: function () {
                window.location = '/';
            }
        });
    });


    // Add new task by manager
    $("#addTaskBtn").click(function (event) {
        event.preventDefault();
        var cookie = JSON.parse($.cookie('taskzone'));

        var data = {};
        var index = $("#assigneeAdd").val();
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

        $.ajax({
            url: 'rest/tasks/add',
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (response) {
                loadPublicTasks();
                $("#titleAdd").val('');
                $("#descriptionAdd").val('');
                $("#deadlineAdd").val('');
            }
        });
    });

    // Add new private task
    $("#addTaskPrivateBtn").click(function (event) {
        event.preventDefault();
        var cookie = JSON.parse($.cookie('taskzone'));

        var data = {};
        data["title"] = $("#titleAddPrivate").val();
        data["description"] = $("#descriptionAddPrivate").val();
        data["deadline"] = $("#deadlineAddPrivate").val();

        $.ajax({
            url: 'rest/tasks/private/add',
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (response) {
                loadPrivateTasks();
                $("#titleAddPrivate").val('');
                $("#descriptionAddPrivate").val('');
                $("#deadlineAddPrivate").val('');
            }
        });
    });


    // Open the task card by click
    var clickedDiv;
    $("#publictasks, #historytasks").on("click", ".card", function() {
        var cookie = JSON.parse($.cookie('taskzone'));

        clickedDiv = $(this).attr('id');
        $.ajax({
            url: 'rest/tasks/edit/' + clickedDiv,
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'GET',
            dataType: 'json',
            contentType: 'application/json',
            success: function (response) {
                var date = new Date(response.deadline).toISOString().substr(0, 10);
                if (response.done) {
                    $("#title").attr('disabled', 'disabled');
                    $("#deadline").attr('disabled', 'disabled');
                    $("#assignee").attr('disabled', 'disabled');
                    $("#description").attr('disabled', 'disabled');
                    $("#comments").attr('disabled', 'disabled');
                    $("#saveTaskBtn").hide();
                } else if (!response.done && currentUser.manager){
                    $("#title").prop('disabled', false);
                    $("#deadline").prop('disabled', false);
                    $("#assignee").prop('disabled', false);
                    $("#description").prop('disabled', false);
                    // $("#comments").prop('disabled', false);
                    $("#saveTaskBtn").show();
                }
                $('#title').val(response.title);
                $('#idUpdate').val(response.id);
                $('#deadline').val(date);
                $('#description').val(response.description);

                $.each(users, function (i, item) {
                   if (item.userId == response.assignee.userId)  {
                       $('#assignee').val(i);
                   }
                });

                if (response.response != null) {
                    $('#comments').val(response.response.comment);
                } else {
                    $('#comments').val('');
                }

                $('#task').modal('show');
            }
        });
        return false;
    });

    // Open the private task card by click
    $("#privatetasks").on("click", ".card", function() {
        var cookie = JSON.parse($.cookie('taskzone'));

        clickedDiv = $(this).attr('id');
        $.ajax({
            url: 'rest/tasks/private/edit/' + clickedDiv,
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'GET',
            dataType: 'json',
            contentType: 'application/json',
            success: function (response) {
                var date = new Date(response.deadline).toISOString().substr(0, 10);
                $('#titlePrivate').val(response.title);
                $('#idUpdatePrivate').val(response.id);
                $('#deadlinePrivate').val(date);
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
        var index = $("#assignee").val();
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
        data["response"] = {
          comment: $("#comments").val(),
          document: $("#file").val()
        };

        $.ajax({
            url: 'rest/tasks/update',
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'PUT',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (response) {
                loadPublicTasks();
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
                loadPrivateTasks();
            }
        });
    });


    // Delete public task by manager
    $("#deleteTaskBtn").click(function (event) {
        event.preventDefault();
        var cookie = JSON.parse($.cookie('taskzone'));
        $.ajax({
            url: 'rest/tasks/remove/' + clickedDiv,
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'DELETE',
            success: function (response) {
                loadPublicTasks();
                loadLastHistory();
                loadHistory();
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
                loadPrivateTasks();
            }
        });
    });


    // Submit public task
    $("#publictasks").on("click", "button", function() {
        var cookie = JSON.parse($.cookie('taskzone'));
        var id = $(this).parent().attr('id');
        $.ajax({
            url: 'rest/tasks/toogle/' + id,
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'GET',
            success: function (response) {
                loadPublicTasks();
                loadLastHistory();
                loadHistory();
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alert("Forbidden! Only assignee person can submit the task");
            }
        });
        return false;
    });

    // Submit private task
    $("#privatetasks").on("click", "button", function() {
        var cookie = JSON.parse($.cookie('taskzone'));
        var id = $(this).parent().attr('id');
        $.ajax({
            url: 'rest/tasks/private/toogle/' + id,
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'GET',
            success: function (response) {
                loadPrivateTasks();
            }
        });
        return false;
    });

    // Add new user
    $("#addUserSubmit").click(function(event) {
        event.preventDefault();
        var cookie = JSON.parse($.cookie('taskzone'));

        var data = {};
        data["username"] = $("#username").val();
        data["firstName"] = $("#firstName").val();
        data["lastName"] = $("#lastName").val();
        data["password"] = $("#password").val();
        data["email"] = $("#email").val();
        $.ajax({
            url: 'rest/user/add',
            data: JSON.stringify(data),
            contentType: 'application/json',
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'POST',
            success: function (response) {
                loadUsers();
                $("#username").val('');
                $("#firstName").val('');
                $("#lastName").val('');
                $("#password").val('');
                $("#email").val('');
            }
        });
    });

    // Update user profile
    $("#editProfileSubmit").click(function (event) {
        event.preventDefault();
        var cookie = JSON.parse($.cookie('taskzone'));

        var data = {};
        data["username"] = $("#usernameEdit").val();
        data["firstName"] = $("#firstNameEdit").val();
        data["lastName"] = $("#lastNameEdit").val();
        data["email"] = $("#emailEdit").val();

        $.ajax({
            url: 'rest/user/edit',
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'PUT',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (data) {
                updateUserProfile(data);
            }
        });
    });

    // Update user password
    $("#changePassSubmit").click(function (event) {
        event.preventDefault();
        var cookie = JSON.parse($.cookie('taskzone'));

        var data = {};
        data["oldPass"] = $("#oldPass").val();
        data["newPass"] = $("#newPass").val();

        $.ajax({
            url: 'rest/user/changePass',
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (data) {
                alert("Password successfully changed!");
                $("#oldPass").val('');
                $("#newPass").val('');
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alert("The old password was incorrect, try again!");
                $("#oldPass").val('');
                $("#newPass").val('');
            }
        });
    });

    // Delete user
    $("#usersCards").on("click", "button", function() {
        var cookie = JSON.parse($.cookie('taskzone'));
        if ($(this).attr('id') == 'deleteUserBtn') {
            var id = $(this).parent().attr('id');
            $.ajax({
                url: 'rest/user/remove/' + id,
                headers: {'X-CSRF-TOKEN': cookie.csrf},
                type: 'DELETE',
                success: function (response) {
                    loadUsers();
                    loadHistory();
                    loadPublicTasks();
                    loadLastHistory();
                }
            });
            return false;
        }
        return false;
    });


    // Open public tasks tab
    $("#publicTab").click(function (event) {
        event.preventDefault();
        $("#publicTab").tab('show');
        $("#privatetasks").hide();
        $("#historytasks").hide();
        $("#publictasks").show();
    });

    // Open private tasks tab
    $("#privateTab").click(function (event) {
        event.preventDefault();
        $("#privateTab").tab('show');
        $("#publictasks").hide();
        $("#historytasks").hide();
        $("#privatetasks").show();
    });

    // Open history
    $("#historyTab").click(function (event) {
        event.preventDefault();
        $("#historyTab").tab('show');
        $("#publictasks").hide();
        $("#privatetasks").hide();
        $("#historytasks").show();
    });

    // Open manage users page
    $("#manageUsersLink").click(function (event) {
        event.preventDefault();
        $("#tasksContainer").hide();
        $("#profileContainer").hide();
        $("#usersContainer").show();
    });

    // Open main page
    $("#homeLink").click(function (event) {
        event.preventDefault();
        $("#usersContainer").hide();
        $("#profileContainer").hide();
        $("#tasksContainer").show();
    });

    // Open profile page
    $("#profileLink").click(function (event) {
        event.preventDefault();
        $("#usersContainer").hide();
        $("#tasksContainer").hide();
        $("#profileContainer").show();
    });


    // Load public tasks
    function loadPublicTasks() {
        var cookie = JSON.parse($.cookie('taskzone'));
        $.ajax({
            url: 'rest/tasks/all',
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'GET',
            success: function (data) {
                $("#dueTasks").children('div').remove();
                for (var i = 0; i < data.length; i++ ) {
                    var date = new Date(data[i].deadline).toLocaleString().split(',')[0];
                    var today = new Date().toLocaleString().split(',')[0];
                    var clForButton = "glyphicon-ok";
                    if (date < today) {
                        clForButton = "glyphicon-ok glyphicon-red";
                    }
                    var div =
                        $('<div id="' + data[i].id + '" class="card"><div class="row"><div class="col-lg-10">' +
                            '<h3>' + data[i].title + '</h3><br>' +
                            '<p><span><i class="fa fa-fw fa-user"></i></span><span>&nbsp;' + data[i].assignee.firstName + ' ' + data[i].assignee.lastName + '</span></p>' +
                            '<p><span><i class="fa fa-fw fa-calendar"></i></span><span>&nbsp;' + date + '</span></p></div>' +
                            '<div class="col-lg-2 v-center">' +
                            '</div></div>' +
                            '<button type="button" class="btn btn-success btn-circle btn-xl outline" id="submitTaskPublic"><i class="glyphicon ' + clForButton +'"></i></button>' +
                            '</div>');
                    $("#dueTasks").append(div);
                }
            }
        });
    }

    // Load private tasks
    function loadPrivateTasks() {
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
                            '<p>Due date: ' + date + '</p>' +
                            '<button type="button" class="btn btn-success btn-circle btn-lg" id="submitTaskPrivate"><i class="glyphicon glyphicon-ok"></i></button></div>');
                    $("#privatetasks").append(div);
                }
            }
        });
    }

    // Load last finished tasks
    function loadLastHistory() {
        var cookie = JSON.parse($.cookie('taskzone'));
        $.ajax({
            url: 'rest/tasks/last',
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'GET',
            success: function (data) {
                $("#recentlyFinished").children('div').remove();
                for (var i = 0; i < data.length; i++ ) {
                    var finished = new Date(data[i].finished).toLocaleString().split(',')[0];
                    var deadline = new Date(data[i].deadline).toLocaleString().split(',')[0];
                    var div =
                        $('<div id="' + data[i].id + '" class="card"><div class="row"><div class="col-lg-10">' +
                            '<h3>' + data[i].title + '</h3><br>' +
                            '<p><span><i class="fa fa-fw fa-user"></i></span><span>&nbsp;' + data[i].assignee.firstName + ' ' + data[i].assignee.lastName + '</span></p>' +
                            '<p><span><i class="fa fa-fw fa-calendar"></i></span><span>&nbsp;' + deadline + '</span><span class="padded-left"><i class="fa fa-fw fa-check-square-o"></i></span><span>&nbsp;' + finished +'</span></p></div>' +
                            '<div class="col-lg-2 v-center">' +
                            '</div></div></div>');
                    $("#recentlyFinished").append(div);
                }
            }
        });
    }

    // Load all finished tasks
    function loadHistory() {
        var cookie = JSON.parse($.cookie('taskzone'));
        $.ajax({
            url: 'rest/tasks/history',
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'GET',
            success: function (data) {
                $("#historytasks").children('div').remove();
                for (var i = 0; i < data.length; i++ ) {
                    var finished = new Date(data[i].finished).toLocaleString().split(',')[0];
                    var deadline = new Date(data[i].deadline).toLocaleString().split(',')[0];
                    var div =
                        $('<div id="' + data[i].id + '" class="card"><div class="row"><div class="col-lg-10">' +
                            '<h3>' + data[i].title + '</h3><br>' +
                            '<p><span><i class="fa fa-fw fa-user"></i></span><span>&nbsp;' + data[i].assignee.firstName + ' ' + data[i].assignee.lastName + '</span></p>' +
                            '<p><span><i class="fa fa-fw fa-calendar"></i></span><span>&nbsp;' + deadline + '</span><span class="padded-left"><i class="fa fa-fw fa-check-square-o"></i></span><span>&nbsp;' + finished +'</span></p></div>' +
                            '<div class="col-lg-2 v-center">' +
                            '</div></div></div>');
                    $("#historytasks").append(div);
                }
            }
        });
    }

    // Load users
    function loadUsers() {
        var cookie = JSON.parse($.cookie('taskzone'));
        $.ajax({
            url: 'rest/user/list',
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            type: 'GET',
            success: function (data) {
                $("#usersCards").children('div').slice(1).remove();
                for (var i = 0; i < data.length; i++ ) {
                    var div =
                        $('<div class="col-sm-12 col-md-4"><div class="card col-md-12"><div class="card-block"><br>' +
                        '<img class="card-img-top center-block" src="img/user.png" alt="User avatar">' +
                        '<h4 class="card-title text-center">' + data[i].lastName + ' ' + data[i].firstName + '</h4><div class="text-left"><br><dl class="row">' +
                        '<dt class="col-md-4">Email:</dt><dd class="col-md-8">' + data[i].email + '</dd><dt class="col-md-4">Phone:</dt><dd class="col-md-8">+3589450990</dd><br><br>' +
                        '</dl><br></div><div id="' + data[i].userId +'" class="text-center"><button type="submit" class="btn btn-info btn-transparent sharp" id="deleteUserBtn">Delete User</button>' +
                        '</div></dl></div></div></div>');
                    $("#usersCards").append(div);

                    users = data;
                    $("#assigneeAdd").children().remove();
                    $("#assignee").children().remove();
                    $.each(data, function (i, item) {
                        $('#assigneeAdd').append($('<option>', {
                            value: i,
                            text : item.firstName + ' ' + item.lastName,
                            id: item.userId
                        }));
                        $('#assignee').append($('<option>', {
                            value: i,
                            text : item.firstName + ' ' + item.lastName,
                            id: item.userId
                        }));
                    });
                }
            }
        });
    }

    // Handler func for user update
    function updateUserProfile(user) {
        $("#profileInfo").children().remove();
        var info =
            $('<dt class="col-md-2">Username:</dt><dd class="col-md-10">' + user.username + '</dd>' +
                '<dt class="col-md-2">First Name:</dt><dd class="col-md-10">' + user.firstName +'</dd>' +
                '<dt class="col-md-2">Last Name:</dt><dd class="col-md-10">' + user.lastName + '</dd>' +
                '<dt class="col-md-2">Email:</dt><dd class="col-md-10">' + user.email +'</dd>');
        $("#profileInfo").append(info);

        $('#usernameEdit').val(user.username);
        $('#firstNameEdit').val(user.firstName);
        $('#lastNameEdit').val(user.lastName);
        $('#emailEdit').val(user.email);
    }

});