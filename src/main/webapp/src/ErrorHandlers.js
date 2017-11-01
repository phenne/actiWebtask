class SessionErrorHandler {

    handleError() {
        location.replace("/login");
    }
}

class AccessErrorHandler {

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

class UserAlreadyExistsErrorHandler {

    constructor() {
        this.message = "User with such username already exists!";
    }

    handleError(field) {
        field.innerHTML = this.message;
    }
}

class UserDeletedErrorHandler {

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

class CurrentUserDeletedErrorHandler {

    handleError() {
        alert("Someone has deleted you!");
        location.replace("/login");
    }
}