import {useHistory, useLocation} from "react-router-dom";
import React, {useEffect, useState} from "react";
import queryString from "query-string";

export function SearchForm({hideAlert}) {
    const history = useHistory();
    const location = useLocation();
    const [minPrice, setMinPrice] = useState("");
    const [maxPrice, setMaxPrice] = useState("");
    const [tags, setTags] = useState("");
    const [searchValue, setSearchValue] = useState("");

    useEffect(() => {
        const queryParams = queryString.parse(location.search);
        if (queryParams.tag) {
            if (typeof queryParams.tag === 'string') {
                setTags(queryParams.tag);
            } else {
                setTags(queryParams.tag.join(","));
            }
        }
        setSearchValue(queryParams.searchValue ? queryParams.searchValue : "");
        setMinPrice(queryParams.minPrice ? queryParams.minPrice : "");
        setMaxPrice(queryParams.maxPrice ? queryParams.maxPrice : "");
    }, [location.search]);

    const handleSubmit = e => {
        e.preventDefault();
        hideAlert();
        let tagArray = null;
        if (tags !== "") {
            tagArray = tags.split(",");
        }
        const sourceParametersString = queryString.parse(location.search);
        const parametersString = queryString.stringify({
            tag: tagArray,
            searchValue: checkForEmptyString(searchValue),
            minPrice: checkForEmptyString(minPrice),
            maxPrice: checkForEmptyString(maxPrice),
            sortField: sourceParametersString.sortField,
            sortType: sourceParametersString.sortType,
            perPage: sourceParametersString.perPage
        }, {skipNull: true});
        history.push(location.pathname + "?" + parametersString);
    };

    const checkForEmptyString = value => {
        return value === "" ? null : value;
    };

    const handleChange = e => {
        if (e.target.name === "minPrice") {
            setMinPrice(e.target.value);
        } else if (e.target.name === "maxPrice") {
            setMaxPrice(e.target.value);
        } else if (e.target.name === "searchValue") {
            setSearchValue(e.target.value);
        } else if (e.target.name === "tag") {
            setTags(e.target.value);
        }
    };

    const handleReset = () => {
        setSearchValue("");
        setTags("");
        setMinPrice("");
        setMaxPrice("");
        history.push(location.pathname);
    };

    const handleKeyDown = e => {
        let field = e.target.name === "minPrice" ? minPrice : maxPrice;
        if (e.key !== "Backspace" && e.key !== "Delete" && e.key !== "ArrowLeft" && e.key !== "ArrowRight") {
            if ((field.includes(".") && e.key.charCodeAt(0) === 46)
                || (!field.includes(".") && field.length > 3 && e.key.charCodeAt(0)
                    !== 46)
                || (field.includes(".") && field.split(".")[1].length > 1
                    && e.target.selectionStart > field.indexOf("."))
                || (field.includes(".") && field.split(".")[0].length > 3
                    && e.target.selectionStart <= field.indexOf("."))
                || ((e.key.charCodeAt(0) < 48 || e.key.charCodeAt(0) > 57)
                    && e.key.charCodeAt(0) !== 46)) {
                e.preventDefault();
            }
        }
    };

    return (
        <form className="search-form" onSubmit={handleSubmit}>
            <div className="form-row">
                <div className="form-group col-md-3">
                    <input maxLength="1000" type="text" className="form-control"
                           onChange={handleChange} value={searchValue}
                           name="searchValue"
                           placeholder="Description"/>
                </div>
                <div className="form-group col-md-3">
                    <input maxLength="500" type="text" className="form-control"
                           onChange={handleChange} value={tags} name="tag"
                           placeholder="Tags: tag1,tag2,tag3"/>
                </div>
                <div className="form-group col-md-1">
                    <input type="text" className="form-control"
                           onKeyDown={handleKeyDown}
                           onChange={handleChange} value={minPrice} name="minPrice"
                           placeholder="Min price: 5"/>
                </div>
                <div className="form-group col-md-1">
                    <input type="text" className="form-control"
                           onKeyDown={handleKeyDown}
                           onChange={handleChange} value={maxPrice} name="maxPrice"
                           placeholder="Max price: 5"/>
                </div>
                <div className="form-group col-md-1">
                    <input type="reset" className="form-control btn btn-danger"
                           onClick={() => handleReset()}
                           value="Reset"/>
                </div>
                <div className="form-group col-md-3">
                    <input type="submit" className="form-control btn btn-primary"
                           value="Search"/>
                </div>
            </div>
        </form>
    )

}