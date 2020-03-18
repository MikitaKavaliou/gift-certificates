import React from 'react';
import ReactDOM from 'react-dom';
import './style/style.scss';
import App from "./app/AppContainer";
import {Provider} from "react-redux";
import {applyMiddleware, createStore} from "redux";
import rootReducer from "./reducers";
import thunk from "redux-thunk";
import subscriber from "./subsriber"

export const store = createStore(rootReducer, {
    token: localStorage.getItem("token")
}, applyMiddleware(thunk));

store.subscribe(subscriber);

ReactDOM.render(
    <Provider store={store}>
        <App/>
    </Provider>, document.getElementById('root'));