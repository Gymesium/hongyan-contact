import http from './http'

export const authApi = {
  register: (payload) => http.post('/auth/register', payload),
  login: (payload) => http.post('/auth/login', payload),
  me: () => http.get('/auth/me')
}

export const majorApi = {
  listEnabled: () => http.get('/majors'),
  listAll: () => http.get('/admin/majors'),
  create: (payload) => http.post('/admin/majors', payload),
  update: (id, payload) => http.put('/admin/majors/' + id, payload),
  disable: (id) => http.delete('/admin/majors/' + id)
}

export const profileApi = {
  mine: () => http.get('/profile'),
  save: (payload) => http.put('/profile', payload)
}

export const directoryApi = {
  search: (params) => http.get('/directory', { params })
}

export const adminApi = {
  accounts: (params) => http.get('/admin/accounts', { params }),
  approve: (id) => http.post('/admin/accounts/' + id + '/approve'),
  reject: (id) => http.post('/admin/accounts/' + id + '/reject'),
  disable: (id) => http.post('/admin/accounts/' + id + '/disable'),
  enable: (id) => http.post('/admin/accounts/' + id + '/enable'),
  remove: (id) => http.delete('/admin/accounts/' + id)
}
