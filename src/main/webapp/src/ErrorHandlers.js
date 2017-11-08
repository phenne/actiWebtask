export class SessionErrorHandler {

    handleError() {
        location.replace("/login");
    }
}

export class AccessErrorHandler {

    constructor() {
        this.message = "You should be a manager to perform this operation!"
    }

    handleError(field) {
        field.innerHTML = this.message;
        setTimeout(() => {
            location.reload(true);
        }, 1500)
    }
}

export class UserAlreadyExistsErrorHandler {

    constructor() {
        this.message = "User with such username already exists!";
    }

    handleError(field) {
        field.innerHTML = this.message;
    }
}

export class UserDeletedErrorHandler {

    constructor() {
        this.message = "User has already been deleted";
    }

    handleError(field) {
        field.innerHTML = this.message;
        setTimeout(() => {
            location.reload(true);
        }, 1500);
    }
}

export class CurrentUserDeletedErrorHandler {

    handleError() {
        alert("Someone has deleted you!");
        location.replace("/login");
    }
}

export class UnknownErrorHandler {

    handleError() {
        alert("Unknown error has occurred");
        location.replace("/");
    }
}