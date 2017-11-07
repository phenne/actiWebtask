import {
    AccessErrorHandler, CurrentUserDeletedErrorHandler, SessionErrorHandler,
    UserAlreadyExistsErrorHandler, UserDeletedErrorHandler
} from "./ErrorHandlers";
import $ from 'jquery';

class SimpleRequest {

    constructor(method) {
        this.method = method;
        this.params = [];
        this.errorHandlers = {
            SESSION_EXPIRED: new SessionErrorHandler(),
            CURRENT_USER_DELETED: new CurrentUserDeletedErrorHandler(),
        };
    }

    async send() {
        let reqResult = await this._request();

        if (reqResult.error) {
            console.log(reqResult.error.msg);
            this._handleError(this.errorHandlers[reqResult.error.msg]);
            return false;
        }

        if (reqResult) {
            return reqResult;
        }
        return true;
    }

    _request() {
        console.log();
        return $.post("/jsonrpc", JSON.stringify({method: "userService." + this.method, params: this.params}));
    }

    _handleError(handler, reqResult) {
        handler.handleError();
    }
}

export class CurrentUserRequest extends SimpleRequest {

    constructor() {
        super("getCurrentUser");
    }
}

export class AllUsersRequest extends SimpleRequest {

    constructor() {
        super("getAllUsers");
    }
}

export class ManagerRequest extends SimpleRequest {

    constructor(method, errorField) {
        super(method);
        this.errorField = errorField;
        this.errorHandlers.MANAGER_ACCESS_REQUIRED = new AccessErrorHandler();
    }

    _handleError(handler, reqResult) {
        handler.handleError(this.errorField);
    }
}


export class EditUserRequest extends ManagerRequest {

    constructor(user, errorField) {
        super("editUser", errorField);
        this.params = [user];
        this.errorHandlers.USER_ALREADY_EXISTS = new UserAlreadyExistsErrorHandler();
        this.errorHandlers.USER_DELETED = new UserDeletedErrorHandler();
    }
}

export class CreateUserRequest extends ManagerRequest {

    constructor(user, errorField) {
        super("createUser", errorField);
        this.params = [user];
        this.errorHandlers.USER_ALREADY_EXISTS = new UserAlreadyExistsErrorHandler();
    }
}

export class DeleteUserRequest extends ManagerRequest {

    constructor(id, errorField) {
        super("deleteUser", errorField);
        this.params = [id];
        this.errorHandlers.USER_DELETED = new UserDeletedErrorHandler();
    }
}