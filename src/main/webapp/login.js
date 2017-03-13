/**
 * Created by Dmitry on 04.03.2017.
 */
jQuery(document).ready(function ($) {
    $('#loginform').submit(function (event) {
        event.preventDefault();

        var cookie = JSON.parse($.cookie('taskzone'));
        var data = 'username=' + $('#username').val() + '&password=' + $('#password').val();
        $.ajax({
            data: data,
            headers: {'X-CSRF-TOKEN': cookie.csrf},
            timeout: 1000,
            type: 'POST',
            url: '/login'

        }).done(function(data, textStatus, jqXHR) {

            window.location = cookie.url;

        }).fail(function(jqXHR, textStatus, errorThrown) {

            alert('Wrong credentials, try again!');

        });
    });
});