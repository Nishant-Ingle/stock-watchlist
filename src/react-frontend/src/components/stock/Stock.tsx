import axios, {AxiosResponse} from 'axios';
import {useState} from 'react';
import {JSX} from "react";
import './Stock.css';

export default function Stock(): JSX.Element {
    const [stocks, setStocks] = useState([] as Stock[]);
    let [search, setSearch] = useState('');
    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => setSearch(e.target.value);
    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        if (search.trim().length === 0) {
            alert('Empty search query, please enter something.');
            return;
        }

        e.preventDefault();
        axios.get(`http://localhost:8080/api/v1/stocks`, {
                headers: {
                    'Accept': 'application/json'
                },
                params: {
                    'search': search
                }
            }).then((resp: AxiosResponse) => {
                const stockData: Stock[] = resp.data;
                console.log(stockData);
                if (stockData.length === 0) {
                    alert('No record found !');
                } else {
                    setStocks(stockData);
                }
            })
            .catch(console.error);
    };

    return (
        <>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <h3 className={'display-4'}> Stocks </h3>
                    <input
                        id='stock-sym-cname'
                        className={'form-control'}
                        type="text"
                        placeholder="Enter Symbol or Company name"
                        value={search}
                        onChange={handleChange} />
                </div>
                <button id={'stock-search-btn'} type="submit" className={'btn btn-primary'}>Search</button>
            </form>
            {stocks.length !== 0 && (
                <table className={'table table-striped'}>
                    <thead>
                    <tr>
                        <th scope={'col'}>#</th>
                        <th scope={'col'}>Symbol</th>
                        <th scope={'col'}>Name</th>
                        <th scope={'col'}>Price</th>
                    </tr>
                    </thead>
                    <tbody>
                    {stocks.map((stock, idx) => (
                        <tr key={stock.id}>
                            <td scope={'row'}>{idx + 1}</td>
                            <td>{stock.symbol}</td>
                            <td>{stock.name}</td>
                            <td>{stock.price}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>)
            }
        </>);
}