import types from "./types";

const putCertificates = value => ({
    type: types.PUT_CERTIFICATES,
    payload: value
});

const putPagesCount = value => ({
    type: types.PUT_PAGES_COUNT,
    payload: value
});

const updateCertificatesStatus = () => ({
    type: types.TRIGGER_CERTIFICATE_UPDATE
});

export default {
    putCertificates,
    putPagesCount,
    updateCertificatesStatus
}