import './App.css';
import Stock from "./components/stock/Stock.tsx";
import {JSX, useState} from "react";
import Watchlist from "./components/watchlist/Watchlist.tsx";

export default function App(): JSX.Element {
    const [activeTab, setActiveTab] = useState('stock');

    return (
        <>
            <div className={'btn-group'}>
                <button className={'btn btn-primary'} onClick={() => setActiveTab('stock')}>Stock</button>
                <button className={'btn btn-secondary'} onClick={() => setActiveTab('watchlist')}>Watchlist</button>
            </div>
            <div id='app-data'>
                {activeTab === 'stock' && (<Stock/>)}
                {activeTab === 'watchlist' && (<Watchlist/>)}
            </div>
        </>
    );
}