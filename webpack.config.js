const path = require('path');
const webpack = require('webpack');

module.exports = {
    entry: ['babel-polyfill', "./src/main/webapp/src/Main.js"],
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, "src/main/webapp/src/dist")
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /(node_modules|bower_components)/,
                use: {
                    loader: "babel-loader",
                    options: {
                        presets: ["babel-preset-latest"]
                    }
                }
            },
        ]
    },
    plugins: [
        new webpack.ProvidePlugin({
            $: "jquery",
            jQuery: "jquery"
        })
    ]
};