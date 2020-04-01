import Actions from "./actions"
import CommonActions from "../../common/redux/actions"

export const fetchCertificates = parameters => async dispatch => {
    await doFetch(parameters, dispatch);
};

const doFetch = async (parameters, dispatch) => {
    try {
        const response = await fetch(
            "https://localhost:8443/api/certificates" + parameters, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json"
                }
            });
        const responseObject = await response.json();
        dispatch(Actions.putCertificates(responseObject.data));
        dispatch(Actions.putPagesCount(responseObject.pagesCount));
    } catch (e) {
        dispatch(CommonActions.putAlertMessage("Server error."));
        dispatch(CommonActions.showAlert());
    }
};

export const deleteCertificate = (token, certificateId, parameters) => async dispatch => {
    try {
        const response = await fetch("https://localhost:8443/api/certificates/" + certificateId, {
            method: "DELETE",
            headers: {
                "Authorization": "Bearer " + token
            }
        });
        if (!response.ok) {
            let responseBody = await response.json();
            dispatch(CommonActions.putAlertMessage(responseBody.errorMessage));
            dispatch(CommonActions.showAlert());
        }
        await doFetch(parameters, dispatch);
    } catch (e) {
        dispatch(CommonActions.putAlertMessage("Server error."));
        dispatch(CommonActions.showAlert());
    }
};

export const createCertificate = (token, certificate, parameters) => async dispatch => {
    try {
        const response = await fetch("https://localhost:8443/api/certificates", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            }, body: JSON.stringify(certificate)
        });
        if (!response.ok) {
            let responseBody = await response.json();
            dispatch(CommonActions.putAlertMessage(responseBody.errorMessage));
            dispatch(CommonActions.showAlert());
        }
        await doFetch(parameters, dispatch);
    } catch (e) {
        dispatch(CommonActions.putAlertMessage("Server error."));
        dispatch(CommonActions.showAlert());
    }
};

export const updateCertificate = (token, certificateId, certificate, parameters) => async dispatch => {
    try {
        const response = await fetch("https://localhost:8443/api/certificates/" + certificateId, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            }, body: JSON.stringify(certificate)
        });
        if (!response.ok) {
            let responseBody = await response.json();
            dispatch(CommonActions.putAlertMessage(responseBody.errorMessage));
            dispatch(CommonActions.showAlert());
        }
        await doFetch(parameters, dispatch);
    } catch (e) {
        dispatch(CommonActions.putAlertMessage("Server error."));
        dispatch(CommonActions.showAlert());
    }
};