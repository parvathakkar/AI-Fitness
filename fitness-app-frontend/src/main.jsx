import React from "react";
import ReactDOM from "react-dom/client";
import {
  AuthContext,
  AuthProvider,
  TAuthConfig,
  TRefreshTokenExpiredEvent,
} from "react-oauth2-code-pkce";
import { authConfig } from "./authConfig.js";
import { Provider } from "react-redux";
import { store } from "./store/store.js";

import App from "./App";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <AuthProvider
    authConfig={authConfig}
    loadingComponent={<div>Loading...</div>}
  >
    <Provider store={store}>
      <App />
    </Provider>
  </AuthProvider>
);
