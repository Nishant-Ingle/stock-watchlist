import {JSX, useEffect, useState} from "react";
import axios, {AxiosResponse} from "axios";

export default function Watchlist(): JSX.Element {
    const [watchlist, setWatchlist] = useState([] as Watchlist[]);
    const [opState, setOpState] = useState<'create' | 'update'>('create');
    const [wlId, setWlId] = useState('');
    const [wlName, setWlName] = useState('');
    const [stocksString, setStocksString] = useState('');

    const putWatchlist = (watchlistId: string, syms: string[]) => {
        console.log(`Sending put req with ${watchlistId} and ${syms}`)

        axios.put(`http://localhost:8080/api/v1/watchlists/${watchlistId}/stocks`,
            syms,
            {
            headers: {
                'Content-Type': 'application/json'
            }
        }).then((resp: AxiosResponse) => {
            resetForm(false);
            console.log(resp.status);
            fetchWatchlist();
        }).catch(console.error);
    };

    const createWatchlist = (name: string, syms: string[]) => {
        console.log(`Sending post req with ${name} and ${syms}`);
        axios.post(`http://localhost:8080/api/v1/watchlists`,
            [{
                name: name,
                stockSyms: syms
            }] as Watchlist[],
            {
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then((resp: AxiosResponse) => {
                resetForm(false);
                console.log(resp.data);
                fetchWatchlist();
        }).catch(console.error);
    };

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setWlName(wlName);
        console.log(wlName);
        console.log(stocksString);

        const stocks = stocksString.toUpperCase().split(',').map(item => item.trim());
        if (opState === 'create') {
            createWatchlist(wlName, stocks);
        } else if (opState === 'update') {
            putWatchlist(wlId, stocks);
        }
    };

    const handleEditWatchlist = (watchlist: Watchlist) => {
        setWlId(watchlist.id);
        setWlName(watchlist.name);
        setStocksString(watchlist.stockSyms.join(', '))
        setOpState('update');
    };

    const resetForm = (resetOp = true) => {
        setWlId('');
        setWlName('');
        setStocksString('');
        if (resetOp)
            setOpState('create');
    }

    const fetchWatchlist = () => {
        axios.get(`http://localhost:8080/api/v1/watchlists`, {
            headers: {
                'Accept': 'application/json'
            }
        }).then((resp: AxiosResponse) => {
            const watchlistData: Watchlist[] = resp.data;
            setWatchlist(watchlistData);
            console.log(watchlistData);
        }).catch(console.error);
    }

    useEffect(() => {
        fetchWatchlist();
    }, []);

    return (
        <div className={'container'}>
            <div className={'row'}>
                <div className={'col'}>
                    {watchlist.length !== 0 && (
                        <table className={'table table-striped'}>
                            <thead>
                            <tr>
                                <th scope={'col'}>#</th>
                                <th scope={'col'}>Name</th>
                                <th scope={'col'}>Symbols</th>
                            </tr>
                            </thead>
                            <tbody>
                            {watchlist.map((watchlist, idx) => (
                                <tr key={watchlist.id}>
                                    <td scope={'row'}>{idx + 1}</td>
                                    <td>{watchlist.name}</td>
                                    <td>{watchlist.stockSyms.join(', ')}</td>
                                    <td>
                                        <button className={'btn btn-light'} onClick={() => handleEditWatchlist(watchlist)}>
                                            Edit
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>)
                    }
                </div>
                <div className={'col'}>
                    <form onSubmit={handleSubmit}>
                        <div className="form-group">
                            <input
                                id='wl-name'
                                disabled={opState === 'update'}
                                className={'form-control'}
                                type="text"
                                placeholder="Enter Watchlist Name"
                                value={wlName}
                                onChange={(e) => setWlName(e.target.value)} />
                            <input
                                id='wl-stocks'
                                className={'form-control'}
                                type="text"
                                placeholder="Enter Watchlist Symbols separated by commas."
                                value={stocksString}
                                onChange={(e) => setStocksString(e.target.value)} />
                        </div>
                        <button id={'wl-create-update-btn'} type="submit" className={'btn btn-primary'}>
                            {opState === 'create' ? 'Create': 'Update'}
                        </button>
                        <button id={'reset'} type={'reset'} className={'btn btn-secondary'} onClick={() => resetForm()}>
                            Reset
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
}