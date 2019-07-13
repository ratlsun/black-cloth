import React, {Component} from 'react';
import { Button } from 'reactstrap';

class Login extends Component {
    constructor(props) {
        super(props);
        this.state = {
          result: '',
        };
    }

    touch = () => {
        fetch("http://localhost:8080/touch", {
            credentials: 'include',
          })
          .then(res => {
            console.log(`Response fetched: ${res.status}`);
            if (res.status === 200) {
                const data = res.text();
                console.log(`Data fetched: ${data}`);
                return data;
            } else {
                return res.statusText;
            }
          })
          .then(
            (result) => {
              this.setState({
                result: result,
              });
            },
            // Note: it's important to handle errors here
            // instead of a catch() block so that we don't swallow
            // exceptions from actual bugs in components.
            (error) => {
              this.setState({
                result: 'error',
              });
            }
          )
      }
    
      login_ok = () => {
        fetch("http://localhost:8080/login", {
            credentials: 'include',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            method: 'POST',
            body: 'username=user&password=user'
          })
          .then(res => {
            console.log(`Response fetched: ${res.status}`);
            if (res.status === 200) {
                const data = res.text();
                console.log(`Data fetched: ${data}`);
                return data;
            } else {
                return res.statusText;
            }
          })
          .then(
            (result) => {
              this.setState({
                result: result,
              });
            },
            // Note: it's important to handle errors here
            // instead of a catch() block so that we don't swallow
            // exceptions from actual bugs in components.
            (error) => {
              this.setState({
                result: 'error',
              });
            }
          )
      }

      login_no = () => {
        fetch("http://localhost:8080/login", {
            credentials: 'include',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            method: 'POST',
            body: 'username=no&password=no'
          })
          .then(res => {
            console.log(`Response fetched: ${res.status}`);
            if (res.status === 200) {
                const data = res.text();
                console.log(`Data fetched: ${data}`);
                return data;
            } else {
                return res.statusText;
            }
          })
          .then(
            (result) => {
              this.setState({
                result: result,
              });
            },
            // Note: it's important to handle errors here
            // instead of a catch() block so that we don't swallow
            // exceptions from actual bugs in components.
            (error) => {
              this.setState({
                result: 'error',
              });
            }
          )
      }

      logout = () => {
        fetch("http://localhost:8080/logout", {
            credentials: 'include',
            method: 'POST',
          })
          .then(res => {
            console.log(`Response fetched: ${res.status}`);
            if (res.status === 200) {
                const data = res.text();
                console.log(`Data fetched: ${data}`);
                return data;
            } else {
                return res.statusText;
            }
          })
          .then(
            (result) => {
              this.setState({
                result: result,
              });
            },
            // Note: it's important to handle errors here
            // instead of a catch() block so that we don't swallow
            // exceptions from actual bugs in components.
            (error) => {
              this.setState({
                result: 'error',
              });
            }
          )
      }

      render() {
        const { result } = this.state;
        return(
            <div>
                <p>{result}</p>

                <Button onClick={this.login_ok}>Login Ok</Button>
                <Button onClick={this.login_no}>Login Failed</Button>
                <Button onClick={this.touch}>Touch</Button>
                <Button onClick={this.logout}>Logout</Button>
            </div>
        )
      }
}

export default Login;
