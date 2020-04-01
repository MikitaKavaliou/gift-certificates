import React from "react";

export function Alert({hideAlert, alertMessage}) {
    return (
        <div className="alert alert-danger alert-dismissible fade show"
             role="alert">
            <strong>{alertMessage}</strong>
            <button type="button" className="close" data-dismiss="alert"
                    onClick={hideAlert} aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    )
}