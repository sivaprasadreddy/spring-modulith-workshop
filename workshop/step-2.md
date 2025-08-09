# 2. Follow package-by-feature

**1. Refactor code to follow package-by-feature**

The existing code can be refactored to have the following structure:

```
bookstore
  |- config
  |- common
  |- catalog
  |   - domain
  |   - web
  |- orders
  |   - domain
  |   - web
  |- inventory
  |- notifications
```

Create the directory structure using the following command:

```shell
mkdir -p {common,catalog/{domain,web},orders/{domain,web},inventory,notifications}
```

**2. Accordingly, move tests to the new structure**


[Next: 3. Add Spring Modulith support](step-3.md)