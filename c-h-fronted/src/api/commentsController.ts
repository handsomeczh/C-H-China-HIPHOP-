// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** addComment POST /api/music/comments/add */
export async function addCommentUsingPost(
  body: API.CommentAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.Result>('/api/music/comments/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    data: body,
    ...(options || {})
  })
}

/** deleteComment DELETE /api/music/comments/delete/${param0} */
export async function deleteCommentUsingDelete(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteCommentUsingDELETEParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.Result>(`/api/music/comments/delete/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {})
  })
}

/** likeComment POST /api/music/comments/like/${param0} */
export async function likeCommentUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.likeCommentUsingPOSTParams,
  options?: { [key: string]: any }
) {
  const { commentId: param0, ...queryParams } = params
  return request<API.Result>(`/api/music/comments/like/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {})
  })
}

/** getCommentsByMusicId GET /api/music/comments/page */
export async function getCommentsByMusicIdUsingGet(
  body: API.CommentPageRequest,
  options?: { [key: string]: any }
) {
  return request<API.ResultPageResult_>('/api/music/comments/page', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    },
    data: body,
    ...(options || {})
  })
}

/** getRepliesByCommentId GET /api/music/comments/reply/ */
export async function getRepliesByCommentIdUsingGet(
  body: API.ReplyPageRequest,
  options?: { [key: string]: any }
) {
  return request<API.ResultPageResult_>('/api/music/comments/reply/', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    },
    data: body,
    ...(options || {})
  })
}

/** addReply POST /api/music/comments/reply/add */
export async function addReplyUsingPost(
  body: API.ReplyAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.Result>('/api/music/comments/reply/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    data: body,
    ...(options || {})
  })
}

/** unlikeComment DELETE /api/music/comments/unlike/${param0} */
export async function unlikeCommentUsingDelete(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.unlikeCommentUsingDELETEParams,
  options?: { [key: string]: any }
) {
  const { commentId: param0, ...queryParams } = params
  return request<API.Result>(`/api/music/comments/unlike/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {})
  })
}
