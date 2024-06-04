// @ts-ignore
/* eslint-disable */
import request from '@/request';

/** add POST /api/playlist/add */
export async function addUsingPost(body: API.PlaylistAddRequest, options?: { [key: string]: any }) {
  return request<API.Result>('/api/playlist/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** cancelSong DELETE /api/playlist/cancel */
export async function cancelSongUsingDelete(
  body: API.PlaylistCancelRequest,
  options?: { [key: string]: any },
) {
  return request<API.Result>('/api/playlist/cancel', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** create POST /api/playlist/create */
export async function createUsingPost(
  body: API.PlaylistCreateRequest,
  options?: { [key: string]: any },
) {
  return request<API.Result>('/api/playlist/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** delete DELETE /api/playlist/delete/${param0} */
export async function deleteUsingDelete1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteUsingDELETE1Params,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.Result>(`/api/playlist/delete/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** getListSongs GET /api/playlist/playlistSong */
export async function getListSongsUsingGet(
  body: API.PlaylistSongGetRequest,
  options?: { [key: string]: any },
) {
  return request<API.ResultPageResult_>('/api/playlist/playlistSong', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** search GET /api/playlist/search */
export async function searchUsingGet1(
  body: API.PlaylistSearchRequest,
  options?: { [key: string]: any },
) {
  return request<API.Result>('/api/playlist/search', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** update POST /api/playlist/update */
export async function updateUsingPost(
  body: API.PlaylistUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.Result>('/api/playlist/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
