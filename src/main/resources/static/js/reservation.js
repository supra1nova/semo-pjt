$(function (){
    // date picker 관련 내용
    let today = new Date();
    let picker = tui.DatePicker.createRangePicker({
        language: 'ko',
        startpicker: {
            date: today,
            weekStartDay: 'Sun',
            input: "#startpicker-input",
            container: "#startpicker-container",
        },
        endpicker: {
            date: today,
            input: '#endpicker-input',
            container: '#endpicker-container',
        },
        format: 'yyyy-MM-dd',
        selectableRanges: [
            [today, new Date(today.getFullYear() + 1, today.getMonth(), today.getDate())]
        ]
    });
    $('#personNumPlusBtn').click(function(){
        $('#personNumInput').val(parseInt($('#personNumInput').val()) + 1);
    })
    $('#personNumMinusBtn').click(function(){
        if(parseInt($('#personNumInput').val()) > 0) {
            $('#personNumInput').val(parseInt($('#personNumInput').val()) - 1);
        }
    })
})