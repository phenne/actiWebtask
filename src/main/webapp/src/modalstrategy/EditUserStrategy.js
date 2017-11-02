class EditUserStrategy {

    constructor(user) {
        this.user = user;
        this.headTitle = "Edit user";
        this.buttonName = "Edit";
        this.passwordRequired = false;
    }

    async prepareModal(fields) {
        $(fields.firstName.parentNode).removeClass("has-error");
        $(fields.lastName.parentNode).removeClass("has-error");
        $(fields.userName.parentNode).removeClass("has-error");
        $(fields.password.parentNode).removeClass("has-error");
        $(fields.confirmPassword.parentNode).removeClass("has-error");
        fields.password.value = "";
        fields.confirmPassword.value = "";
        fields.manager.checked = false;
        fields.manager.disabled = false;

        fields.firstName.value = this.user.firstName;
        fields.lastName.value = this.user.lastName;
        fields.userName.value = this.user.userName;

        let currentUserReqResult = await new CurrentUserRequest().send();
        let currentUserResult = currentUserReqResult.result;

        if (this.user.manager) {
            fields.manager.checked = true;
        }
        if (this.user.userName === currentUserReqResult.userName) {
            fields.manager.disabled = true;
        }

        if (currentUserResult.userName === this.user.userName) {
            fields.manager.checked = true;
            fields.manager.disabled = true;
        } else if (this.user.manager) {
            fields.manager.checked = true;
        }
    };

    async sendActionRequest(fields) {
        let user = new User(
            fields.firstName.value,
            fields.lastName.value,
            fields.userName.value,
            fields.password.value,
            fields.manager.checked,
        );

        user.userId = this.user.id;

        return new EditUserRequest(user, fields.errorMessage).send();
    }

    //TODO выделить в общий механизм
    afterRequestAction() {
        $("#successAlert").show();
        new TableActions().clearTable();
        new TableActions().generateTable();
        setTimeout(() => {
            $("#successAlert").hide();
        }, 3000);
    };
}