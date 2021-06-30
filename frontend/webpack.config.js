// webpack v5
const path = require('path');
const webpack = require("webpack");
// const nodeExternals = require('webpack-node-externals');
const HtmlReplaceWebpackPlugin = require("html-replace-webpack-plugin");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const {CleanWebpackPlugin} = require('clean-webpack-plugin');
var CopyWebpackPlugin = require("copy-webpack-plugin");

const outputPath = "public/graph/";

module.exports = (env, argv) => {

    const devMode = argv.mode !== 'production';

    const config = {
        entry: {
            webvowl: "./src/components/graph/src/webvowl/js/entry.js",
            "webvowl.app": "./src/components/graph/src/app/js/entry.js"
        },
        output: {
            path: path.resolve(__dirname, outputPath),
            publicPath: "",
            filename: 'js/[name].js',
            chunkFilename: "js/[chunkhash].js",
            libraryTarget: "assign",
            library: "[name]"
        },
        devtool: devMode ? "eval" : "source-map",

        module: {
            rules: [

                {
                    test: /\.html$/,
                    loader: "html-loader",
                },
                {
                    test: /\.css$/,
                    use: [
                        devMode ? 'style-loader' : MiniCssExtractPlugin.loader,
                        'css-loader',
                        'postcss-loader'
                    ]
                }
            ]
        },
        plugins: [
            new CleanWebpackPlugin(),
            new MiniCssExtractPlugin({
                filename:  'css/[name].css',
            }),
            new HtmlWebpackPlugin({
                inject: false,
                hash: true,
                template: './src/components/graph/src/index.html',
                filename: 'index2.html'
            }),
            new HtmlReplaceWebpackPlugin({
                pattern: "<%= version %>",
                replacement: require("./package.json").version
            }),
            new CopyWebpackPlugin({
              patterns: [
                { context: "src/components/graph/src/app", from: "data/**/*"},
                { from: "node_modules/d3/d3.min.js",to: "js"}
            ]}),
            new webpack.ProvidePlugin({
                d3: "d3",
            })

        ],
        externals: {
            d3: "d3",
            Global: "Global",
            Namespaces: "Namespaces",
            $:"$",
            Tabulator:"Tabulator",
        },
        devServer: {
            contentBase: path.join(__dirname, outputPath),
            compress: true,
            hot: true,
            historyApiFallback: true,
            inline: true,
            watchContentBase: true,
            open: true,
            port: 8000
        },
        watchOptions: {
            ignored: /node_modules/
        }
    };

    if (devMode) {
        config.plugins.push(
            new webpack.HotModuleReplacementPlugin()
        );
    }


    return config;
};
