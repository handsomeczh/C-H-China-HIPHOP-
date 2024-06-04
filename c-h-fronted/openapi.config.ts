const { generateService } = require('@umijs/openapi')

generateService({
  requestLibPath: "import request from '@/request'", //用于指定在生成的服务代码中导入请求库的路径
  schemaPath: 'http://localhost:8101/api/v2/api-docs',
  serversPath: './src'
})
