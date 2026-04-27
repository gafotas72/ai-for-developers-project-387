# Call Booking Calendar — TypeSpec Project

## Structure
- `prompts/*` - Stored prompts
- `spec/main.tsp` — TypeSpec source defining the Appointment Management API
- `spec/tspconfig.yaml` — Emits OpenAPI 3.0 via `@typespec/openapi3`
- `spec/tsp-output/@typespec/openapi3/openapi.yaml` — generated output
- `spec/package.json` — npm dependencies
- `frontend` - frontend web application that uses rest api requests to the backend
- `backend` - backend rest api server that uses openapi.yaml to implement rest api interface

## Commands

```bash
cd spec && npm install
npx tsp compile   # generates openapi.yaml
```

CI runs: `npm install && tsp compile`

## Notes

- `tsp-output/` is gitignored — output generated locally during CI
- Use `npm` (pinned to 11.12.1)
