// @ts-ignore
/* eslint-disable */
import request from '@/request';

/** cancelFan DELETE /api/user/cancelFan/${param0} */
export async function cancelFanUsingDelete(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.cancelFanUsingDELETEParams,
  options?: { [key: string]: any },
) {
  const { fanId: param0, ...queryParams } = params;
  return request<API.Result>(`/api/user/cancelFan/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** cancelFollow DELETE /api/user/cancelFollow/${param0} */
export async function cancelFollowUsingDelete(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.cancelFollowUsingDELETEParams,
  options?: { [key: string]: any },
) {
  const { followId: param0, ...queryParams } = params;
  return request<API.Result>(`/api/user/cancelFollow/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** getFan GET /api/user/fan */
export async function getFanUsingGet(body: API.PageRequest, options?: { [key: string]: any }) {
  return request<API.ResultPageResult_>('/api/user/fan', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getFollow GET /api/user/follow */
export async function getFollowUsingGet(body: API.PageRequest, options?: { [key: string]: any }) {
  return request<API.ResultPageResult_>('/api/user/follow', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** follow POST /api/user/follow/${param0} */
export async function followUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.followUsingPOSTParams,
  options?: { [key: string]: any },
) {
  const { followId: param0, ...queryParams } = params;
  return request<API.Result>(`/api/user/follow/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** getCode GET /api/user/getCode/${param0} */
export async function getCodeUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getCodeUsingGETParams,
  options?: { [key: string]: any },
) {
  const { phone: param0, ...queryParams } = params;
  return request<API.Result>(`/api/user/getCode/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** login GET /api/user/login */
export async function loginUsingGet(body: API.User_, options?: { [key: string]: any }) {
  return request<API.ResultString_>('/api/user/login', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getLoginCode GET /api/user/login/${param0} */
export async function getLoginCodeUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getLoginCodeUsingGETParams,
  options?: { [key: string]: any },
) {
  const { phone: param0, ...queryParams } = params;
  return request<API.ResultString_>(`/api/user/login/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** loginByCode GET /api/user/loginByCode */
export async function loginByCodeUsingGet(body: API.User_, options?: { [key: string]: any }) {
  return request<API.Result>('/api/user/loginByCode', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** register POST /api/user/register */
export async function registerUsingPost(
  body: API.UserRegisterRequest,
  options?: { [key: string]: any },
) {
  return request<API.Result>('/api/user/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateUser PUT /api/user/update */
export async function updateUserUsingPut(
  body: API.UserUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.Result>('/api/user/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
