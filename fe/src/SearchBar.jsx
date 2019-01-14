import React from 'react'

import { TextField, TextFieldIcon } from '@rmwc/textfield'
import '@material/textfield/dist/mdc.textfield.css'

import { Button } from '@rmwc/button'
import '@material/button/dist/mdc.button.css'

import './search-bar.css'

export default class SearchBar extends React.Component {
  state = {
    keywords: '',
  }

  changeKeywords = (e) => {
    const keywords = e.target.value
    this.setState(prev => ({ ...prev, keywords }))
  }

  clearKeywords = () => {
    this.setState(prev => ({ ...prev, keywords: '' }))
  }

  handleSearch = () => {
    if (keywords.trim() === '') {
      return
    }

    const { fetchAnimations } = this.props
    const { keywords } = this.state
    fetchAnimations(keywords.trim())
  }

  render() {
    const { keywords } = this.state
    

    return (
      <div className="search-group">
        <TextField
          className="search-textfield"
          withLeadingIcon="search"
          label="搜索其实很简单..."
          withTrailingIcon={
            <TextFieldIcon
              tabIndex="0"
              icon="close"
              onClick={this.clearKeywords}/>
          } 
          value={keywords}
          onChange={this.changeKeywords}
        />
        <Button
          raised
          className="search-button"
          onClick={this.handleSearch}
        >
          搜索
        </Button>
      </div>
    )
  }
}
