import React from 'react'

import {
  Card,
  CardPrimaryAction,
  CardMedia,
  CardAction,
  CardActions,
  CardActionButtons,
  CardActionIcons
} from '@rmwc/card';

import { Typography } from '@rmwc/typography'

import '@material/card/dist/mdc.card.css'
import '@material/button/dist/mdc.button.css'
import '@material/icon-button/dist/mdc.icon-button.css'

export default function Animation({ animation }) {
  return (
    <Card style={{ margin: '0px 12px 16px 0', width: '21rem', justifyContent: 'space-between' }}>
      <CardPrimaryAction>
        <div style={{ padding: '0 1rem 1rem 1rem' }}>
          <img style={{ width: '100%' }} src={animation.cover} />
          <Typography use="headline6" tag="h2" dangerouslySetInnerHTML={{ __html: animation.title }}>
          </Typography>
          <Typography
            use="subtitle2"
            tag="h3"
            theme="text-secondary-on-background"
            style={{ marginTop: '-1rem' }}
          >
            {animation.originName || '未知'}
          </Typography>
          <Typography use="body1" tag="div" theme="text-secondary-on-background" dangerouslySetInnerHTML={{ __html: animation.intro || '暂无介绍' }}>
          </Typography>
        </div>
      </CardPrimaryAction>
      <CardActions>
        <CardActionButtons>
          <CardAction onClick={() => { window.open(animation.biliAddress) }}>detail</CardAction>
        </CardActionButtons>
        <CardActionIcons>
          <CardAction
            onIcon="favorite"
            icon="favorite_border"
          />
        </CardActionIcons>
      </CardActions>
    </Card>
  )
}
