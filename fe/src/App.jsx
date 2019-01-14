import React from 'react'

import { Typography } from '@rmwc/typography'
import '@material/typography/dist/mdc.typography.css'
import './app.css'

import SearchBar from './SearchBar'

import AnimationContainer from './AnimationContainer'

const SEARCH_ENTRY = '/search'

export default class App extends React.Component {
  state = {
    loading: false,
    hasSearched: false,
    animations: []
  }

  fetchAnimations = async (keywords, page = 0) => {
    this.setState(prev => ({
      ...prev,
      loading: true,
    }))
    const res = await fetch(`${SEARCH_ENTRY}?q=${keywords}&page=${page}`)
    const animations = await res.json()
    this.setState(prev => ({
      ...prev,
      loading: false,
      hasSearched: true,
      animations,
    }))
  }

  appendAnimations = (animations) => {
    this.setState(prev => ({ ...prev, animations: [...prev.animations, animations] }))
  }

  render() {
    const { fetchAnimations, state: { loading, animations } } = this

    return (
      <div>
        <div className="app-title">
          <Typography use="headline5">Anime 搜索</Typography>
        </div>
        <SearchBar fetchAnimations={fetchAnimations} />
        { !this.isLoading && this.state.hasSearched
            ? <AnimationContainer animations={animations} />
            : null }
      </div>
    )
  }
}

