import React, {useEffect} from "react";
import {useHistory} from "react-router-dom";
import {Form, Formik, useField} from "formik";
import * as Yup from "yup"

export default function LoginComponent({token, refreshToken, loginFailure, validateToken, hideAlert, logIn}) {
    const history = useHistory();

    useEffect(() => {
        if (token) {
            validateToken(token, refreshToken);
            if (token) {
                history.push("/certificates");
            }
        }
    }, [history, refreshToken, token, validateToken]);

    const handleSubmit = values => {
        hideAlert();
        logIn(values);
    };

    const Input = ({label, ...props}) => {
        const [field, meta] = useField(props);
        return (
            <>
                <input {...field} {...props} />
                <div className="error">{meta.touched && meta.error}</div>
            </>
        );
    };

    return (
        <main>
            <div className="login-container">
                <h1 className="login-header">LogIn</h1>
                <Formik
                    initialValues={{username: '', password: ''}}
                    validationSchema={Yup.object({
                        username: Yup.string()
                            .max(30, 'Your username is incorrect')
                            .min(3, 'Your username is incorrect')
                            .required('Your username is incorrect'),
                        password: Yup.string()
                            .max(30, 'Your password is incorrect')
                            .min(4, 'Your password is incorrect')
                            .required('Your username is incorrect'),
                    })}
                    onSubmit={(values) => handleSubmit(values)}
                >
                    {({
                          isValid,
                          dirty
                      }) => (
                        <Form>
                            <Input
                                name="username"
                                type="text"
                                placeholder="Username"
                            />
                            <Input
                                name="password"
                                type="password"
                                placeholder="Password"
                            />
                            <div className="error">
                                <small>{loginFailure &&
                                <div className="error">Wrong username or password</div>}</small>
                            </div>
                            <input disabled={!(isValid && dirty)} className="btn btn-primary" type="submit"
                                   value="Login"/>
                        </Form>
                    )}
                </Formik>
            </div>
        </main>
    )
}