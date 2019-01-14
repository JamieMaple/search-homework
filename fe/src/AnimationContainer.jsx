import React from 'react'

import Animation from './Animation'

export default function AnimationContainer({ animations }) {
  return (
    <div className="animation-container" style={{ display: 'flex', flexWrap: 'wrap', marginTop: 22 }}>
      {
        animations.map(animation => <Animation key={animation.id} animation={animation} />)
      }
    </div>
  )
}
