/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html, ts}",
    "./node_modules/tw-elements/dist/js/**/*.js"
  ],
  theme: {
    extend: {
      colors: {
        'nomad-light': '#CDC5B4',
        'nomad-pink': "#B59DA4",
        'nomad-gray': "#F4F4F4",
        'text-black': "#525252",
        'nomad-dark': '#551B14',
        'nomad-darker': '#421510',
        'nomad-nav-bar':'#C3BBAB',
        'nomad-filter':'#afa897',
        'nomad-filter-hover':'#948d80',
        //a69f90
        'nomad-gray2':'#afa4a4',
        'nomad-gray-hover':'#a89b9b'
      },
      screens: {
        'my-md': '1215px'
      }
    },
  },
  plugins: [require("tw-elements/dist/plugin.cjs"),],
}

