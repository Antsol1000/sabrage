import React from "react";

function GreatDate(props) {
    const d = new Date(props.timestamp);

    return <p className="Great-date">{d.toUTCString()}</p>;
}

function TimeLeft(props) {
    const diff = props.timestamp - Date.now();
    const h = String(Math.floor(diff / 3600000)).padStart(2, "0");
    const m = String(Math.floor(diff / 60000) % 60).padStart(2, "0");
    const s = String(Math.floor(diff / 1000) % 60).padStart(2, "0");

    if (diff < 0) {
        return <p className="Time-left-closed">Applications closed</p>;
    } else {
        return <p className="Time-left">Applications closing in: {h}:{m}:{s}</p>;
    }
}

export {GreatDate, TimeLeft};