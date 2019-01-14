const htmlWebpackPlugin = require('html-webpack-plugin')
const path = require('path')

module.exports = {
  entry: ['babel-polyfill', './src/index.js'],
  mode: 'development',
  output: {
    path: path.resolve(__dirname, 'dist'),
    publicPath: '',
    filename: 'js/[name].[hash:7].js'
  },
  resolve: {
    extensions: ['.ts', '.js', '.tsx', '.jsx'],
  },
  plugins: [
    new htmlWebpackPlugin({
      template: 'index.pug'
    })
  ],
  devServer: {
    contentBase: path.resolve(__dirname, 'dist'),
    compress: true,
    port: 8081,
    proxy: {
      '/search': 'http://localhost:2333'
    }
  },
  module: {
    rules: [
      {
        test: /\.jsx?/,
        exclude: /node_modules/,
        use: ['babel-loader', 'eslint-loader'],
      },
      {
        test: /\.css/,
        use: ['style-loader', 'css-loader']
      },
      {
        test: /\.pug/,
        use: ['pug-loader'],
      }
    ]
  }
}
