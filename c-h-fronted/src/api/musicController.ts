// @ts-ignore
/* eslint-disable */
import request from '@/request';

/** delete DELETE /api/music/delete/${param0} */
export async function deleteUsingDelete(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteUsingDELETEParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.Result>(`/api/music/delete/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** download POST /api/music/download/${param0} */
export async function downloadUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.downloadUsingPOSTParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.Result>(`/api/music/download/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** downloaded GET /api/music/downloaded */
export async function downloadedUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.downloadedUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.ResultListDownloadMusicVo_>('/api/music/downloaded', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** hot POST /api/music/hot */
export async function hotUsingPost(options?: { [key: string]: any }) {
  return request<API.ResultList_>('/api/music/hot', {
    method: 'POST',
    ...(options || {}),
  });
}

/** search GET /api/music/search */
export async function searchUsingGet(
  body: API.MusicSearchRequest,
  options?: { [key: string]: any },
) {
  return request<API.ResultPageResult_>('/api/music/search', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** share GET /api/music/share/${param0} */
export async function shareUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.shareUsingGETParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.ResultString_>(`/api/music/share/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** uploadMusic POST /api/music/upload */
export async function uploadMusicUsingPost(
  body: API.MusicUploadRequest,
  options?: { [key: string]: any },
) {
  return request<API.Result>('/api/music/upload', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getUrl GET /api/music/url/${param0} */
export async function getUrlUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUrlUsingGETParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.ResultUrlVo_>(`/api/music/url/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}
