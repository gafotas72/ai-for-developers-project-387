import express from 'express';
import { createProxyMiddleware } from 'http-proxy-middleware';
import path from 'path';
import { fileURLToPath } from 'node:url';
import { createServer as createViteServer } from 'vite';

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const isProd = process.env.NODE_ENV === 'production';
const app = express();

app.use(
  '/api',
  createProxyMiddleware({
    target: 'http://localhost:8080',
    changeOrigin: true,
  })
);

const baseDir = path.resolve(__dirname);
const distPath = path.join(baseDir, '../dist');

app.use(express.static(distPath));

app.get('*', (req, res) => {
  res.sendFile(path.join(distPath, 'index.html'));
});

const port = process.env.PORT || 3000;
const host = process.env.HOST || '0.0.0.0';
app.listen(port, host, () => {
  console.log(`Server running on http://${host}:${port}`);
});