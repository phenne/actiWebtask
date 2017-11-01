// import {CreateUserRequest} from "../RequestSender";
// import TableActions from "../UserListPageActions";
// import User from "../user";

class CreateUserStrategy {

    constructor() {
        this.headTitle = "Create user";
        this.buttonName = "Create";
        this.passwordRequired = true;
    }

    prepareModal(fields) {
        fields.firstName.value = "";
        $(fields.firstName.parentNode).removeClass("has-error");

        fields.lastName.value = "";
        $(fields.lastName.parentNode).removeClass("has-error");

        fields.userName.value = "";
        $(fields.userName.parentNode).removeClass("has-error");

        fields.password.value = "";
        $(fields.password.parentNode).removeClass("has-error");

        fields.confirmPassword.value = "";
        $(fields.confirmPassword.parentNode).removeClass("has-error");

        fields.manager.checked = false;
    }

    async sendActionRequest(fields) {
        let user = new User(fields.firstName.value, fields.lastName.value, fields.userName.value, fields.password.value, fields.manager.checked);
        return new CreateUserRequest(user, fields.errorMessage).send();
    }

    //TODO выделить общий механизм
    afterRequestAction() {
        $("#successAlert").show();
        new TableActions().clearTable();
        new TableActions().generateTable();
        setTimeout(() => {
            $("#successAlert").hide();
        }, 3000);
    }
}