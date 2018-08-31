# api-changelog

## 2018-08-29接口变更点

### `POST` /api/examines

``请求``

``新增 examineType``

```txt
examineType=TRUE_OR_FALSE//判断题
examineType=MULTIPLE_CHOICE//多选题

```

```响应```

``增加多选项``

```json
 "multipleChoices": [ // 反序列化后的options
        {
          "content": "string", // 选项文本
          "no": "string", // 选项号
          "point": "string" // 选项分值
        }
      ],
 "options": String // JSON.stringify(multipleChoices)
```

### `PUT` /api/examines/{examineId}/questions/{submit}

```请求```

``questionVMList``

```json
"multipleChoiceAnswers": [
      "string"
    ]// 填入选项的选项号
```

### `GET` /api/exmaines

```响应```

同 `POST` /api/examines

### `GET` /api/statistics/sortdepartment/{feature}/{order}

```请求```

``feature & order``

```txt
feature=1/avg-score 获取错误个数/获取平均分
order=asc/desc 倒数前十/正数前十
```

### `subject-resource` 的所有接口

请求增加了`options`
响应增加了`multipleChoices`，不再赘述。

```json
 "multipleChoices": [ // 反序列化后的options
        {
          "content": "string", // 选项文本
          "no": "string", // 选项号
          "point": "string" // 选项分值
        }
      ],
 "options": String // JSON.stringify(multipleChoices)
```