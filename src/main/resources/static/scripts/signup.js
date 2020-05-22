const app = new Vue(
    {
        el: "#vue_div",
        data: {
            username: "",
            display_name: "",
            password: "",
            email: "",
            error_message: ""
        },
        methods: {
            signupButtonClicked: function() {
                var self = this;

                apiCreateUser(this.username, this.display_name, this.password, this.email)
                    .then(function() {
                        console.log("Success.");
                        window.location.href = "/html/login.html";
                    })
                    .catch(function(error_message) {
                        console.log("Failure.");
                        self.error_message = error_message;
                    });
            }
        }
    }
);