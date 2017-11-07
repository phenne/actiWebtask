import $ from "jquery";
import {CreateUserRequest} from "../RequestSender";
import User from "../User";


export default class CreateUserStrategy {

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
}