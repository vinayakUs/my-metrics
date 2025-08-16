let account = window.initialAccount || {};
if (!account.incomes) account.incomes = []; // Safety check



function addItem(){
    const amount = parseFloat(document.getElementById("income-amount-input").value);
    const currency = document.getElementById("income-currency-select").value;
    const period = document.getElementById("income-type-select").value;
    const icon = document.getElementById("incomeIconInput").value;
    const title = document.querySelector('input[title="title-income-input"]').value;




    const newItem = {
        title,
        amount,
        currency,
        period,
        icon
    };

    account.incomes.push(newItem);
    toggleModal('income',false)




    console.log(newItem)
    console.log(account)


}



function toggleModal(type, open) {
    const modalId = type === 'income' ? 'incomeModal' : 'expenseModal';
    const iconWrapperId = type === 'income' ? 'incomeIconWrapper' : 'expenseIconWrapper';

    const modal = document.getElementById(modalId);
    const iconWrapper = document.getElementById(iconWrapperId);

    if (!modal) return;

    if (open) {
        modal.classList.add('show');
    } else {
        modal.classList.remove('show');
        if (iconWrapper) iconWrapper.classList.remove('show')
    }


}


$(document).ready(function () {
    $('#incomeIconTrigger').on('click', function () {
        $('#incomeIconWrapper').toggleClass('show');
    });

    $('#incomeIconGrid .icon-option').on('click', function () {

        const iconText = $(this).text();
        $('#incomeIconTrigger').text(iconText);
        $('#incomeIconInput').val(iconText);
        $('#incomeIconWrapper').removeClass('show');

    });


    $('#expenseIconTrigger').on('click', function () {
        $('#expenseIconWrapper').toggleClass('show');
    });

    $('#expenseIconGrid .icon-option').on('click', function () {

        const iconText = $(this).text();
        $('#expenseIconTrigger').text(iconText);
        $('#expenseIconInput').val(iconText);
        $('#expenseIconWrapper').removeClass('show');

    });


    $("#save-account-change-button").click(function (e) {
        e.preventDefault();
        console.log("Clicked");
        const clientId = 'downstream-client';

        console.log(JSON.stringify(account))

        $.ajax({
            url: `/client-status?clientId=${encodeURIComponent(clientId)}`,
            type: 'GET',
            contentType: 'application/json',
            success: function (status) {
                if (status.connected) {
                    console.log(`Already connected to ${clientId}, saving directly...`);
                    saveData();
                } else {
                    console.log(`Not connected to ${clientId}, starting OAuth...`);
                    startOAuthFlow(clientId);
                }
            },

            error: function (xhr, status, error) {
                console.error("Status check failed:", error);
            }
        });
    });

    function saveData() {
        fetch('/account', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(account)
        })
            .then(response => response)
            .then(data => console.log("Saved successfully:", data))
            .catch(err => console.error("Save failed:", err));
    }
    function startOAuthFlow(clientId) {
        const authWindow = window.open(`/oauth2/authorization/${clientId}`, 'oauth2Login', 'width=100vh,height=100vh');

        const checkAuth = setInterval(() => {
            if (authWindow.closed) {
                clearInterval(checkAuth);
                saveData();
            }
        }, 1000);
    }


});
