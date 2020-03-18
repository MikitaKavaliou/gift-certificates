import Actions from "./actions"

export const fetchCertificates = parameters => async dispatch => {
    const response = await fetch(
        "https://localhost:8443/certificates" + parameters, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        });
    const responseObject = await response.json();
    dispatch(Actions.putCertificates(responseObject.data));
    dispatch(Actions.putPagesCount(responseObject.pagesCount));
};
export const updateCertificatesStatus = Actions.updateCertificatesStatus;
