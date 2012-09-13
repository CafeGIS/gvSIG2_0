import unittest
import introspect

class IntrospectTestCase(unittest.TestCase):

    def testUnboundMethod(self):
        import string
        method = 'string.index('
        (name, argspec, tip) = introspect.getCallTip(method, locals())

        self.assertEquals('index', name)
        self.assertEquals('s, *args', argspec)
        self.assertEquals('index(s, *args)\n\nindex(s, sub [,start [,end]]) -> int\n\nLike find but raises ValueError when the substring is not found.', tip)

    def testBuiltinFunction(self):
        method = 'len('
        print introspect.getCallTip(method, locals())
        
        (name, argspec, tip) = introspect.getCallTip(method, locals())
        
        self.assertEquals('len', name)
        # next 2 assertions fail in Jython!
        self.assertEquals('len(object) -> integer', argspec)
        self.assertEquals('len(object) -> integer\n\nReturn the number of items of a sequence or mapping', tip)


if __name__ == '__main__':
    unittest.main()
