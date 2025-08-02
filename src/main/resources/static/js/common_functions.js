function $get() {
    return $.ajax({
        type: 'GET',
        url: url,
        contentType: 'application/json',
        dataType: 'json'
    }).fail( function (e) {
        var data = e.responseJSON;
        console.error(e);
        alert("오류가 발생하였습니다!!\n" + data.message + "\n" + data.trace);
    }).done( function (data) {
        if (data.status != 'ok') {
          console.error(data);
            alert("오류가 발생하였습니다.\n" + data.msg);
        }
        else {
            onSuccess(data);
        }
    });
}
function $post(url, requestData, onSuccess) {
    return $.ajax({
        type: 'POST',
        url: url,
        data: JSON.stringify(requestData),
        contentType: 'application/json',
        dataType: 'json'
    }).fail( function (e) {
        var data = e.responseJSON;
        console.error(e);
        alert("오류가 발생하였습니다!!\n" + data.message + "\n" + data.trace);
    }).done( function (data) {
        if (data.status != 'ok') {
           console.error(data);
            alert("오류가 발생하였습니다.\n" + data.msg);
        }
        else {
            onSuccess(data);
        }
    });
}

function refresh() {
    window.location = window.location;
}